package com.example.projectbackend.service.impl;

import com.example.projectbackend.bean.request.BookingDetailRequest;
import com.example.projectbackend.bean.response.BookingDetailResponse;
import com.example.projectbackend.entity.*;
import com.example.projectbackend.exception.NotFoundException;
import com.example.projectbackend.mapper.BookingDetailMapper;
import com.example.projectbackend.repository.*;
import com.example.projectbackend.service.BookingDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookingDetailServiceImpl implements BookingDetailService {
    private final BookingDetailRepository bookingDetailRepository;
    private final RoomRepository roomRepository;
    private final BookingRepository bookingRepository;
    private final ProvisionRepository provisionRepository;
    private final RelProvisionBookingDetailRepository relProvisionBookingDetailRepository;

    @Override
    public Page<BookingDetailResponse> getAllBookingDetails(Pageable pageable, String keyword, Long bookingId) {
        Specification<BookingDetail> spec = Specification.where(searchByKeyword(keyword))
                .and(hasBookingId(bookingId));
        Page<BookingDetail> bookingDetailPage = bookingDetailRepository.findAll(spec, pageable);

        return bookingDetailPage.map(BookingDetailMapper::convertToResponse);
    }

    @Override
    public BookingDetailResponse getDetailBookingDetail(Long bookingDetailId) {
        BookingDetail bookingDetail = bookingDetailRepository.findById(bookingDetailId).orElse(null);
        if(Objects.isNull(bookingDetail)){
            throw new NotFoundException("BookingDetailNotFound","Not found BookingDetail with " + bookingDetailId);

        }
        return BookingDetailMapper.convertToResponse(bookingDetailRepository.findById(bookingDetailId).orElse(null));
    }

    @Override
    public BookingDetail createBookingDetail(BookingDetailRequest bookingDetailRequest, Long bookingId) {
        BookingDetail bookingDetail = BookingDetailMapper.convertFromRequest(bookingDetailRequest, provisionRepository);

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("BookingNotFound", "Booking not found with ID: " + bookingId));
        bookingDetail.setBooking(booking);

        Room room = roomRepository.findById(bookingDetailRequest.getRoomId())
                .orElseThrow(() -> new NotFoundException("RoomNotFound", "Room not found with ID: " + bookingDetailRequest.getRoomId()));
        bookingDetail.setRoom(room);

        boolean isRoomAvailable = checkRoomAvailability(room, bookingDetailRequest.getCheckIn(), bookingDetailRequest.getCheckOut());
        if (!isRoomAvailable) {
            throw new IllegalArgumentException("The room is already booked for the dates you requested.");
        }

        room.setStatus(Room.RoomStatus.BOOKED);
        roomRepository.save(room);

        bookingDetail.setStatus(BookingDetail.BookingDetailStatus.PENDING);

        bookingDetail = bookingDetailRepository.save(bookingDetail);

        relProvisionBookingDetailRepository.saveAll(bookingDetail.getRelProvisionBookingDetails());

        return bookingDetail;
    }

    private boolean checkRoomAvailability(Room room, LocalDateTime checkIn, LocalDateTime checkOut) {
        List<BookingDetail> overlappingBookings = bookingDetailRepository.findOverlappingBookings(room.getRoomId(), checkIn, checkOut);
        return overlappingBookings.isEmpty();
    }

    @Override
    public BookingDetail updateBookingDetail(Long bookingDetailId, BookingDetail bookingDetail) {
        BookingDetail bookingDetailUpdate = bookingDetailRepository.findById(bookingDetailId)
                .orElseThrow(() -> new NotFoundException("BookingDetailNotFound", "Booking Detail not found with ID: " + bookingDetailId));

        setBookingDetail(bookingDetailUpdate, bookingDetail);

        if (bookingDetailUpdate.getStatus() == BookingDetail.BookingDetailStatus.CONFIRMED) {
            Room room = bookingDetailUpdate.getRoom();

            if (room.getStatus() != Room.RoomStatus.AVAILABLE) {
                room.setStatus(Room.RoomStatus.AVAILABLE);
                roomRepository.save(room);
            }
        }

        if (bookingDetailUpdate.getStatus() == BookingDetail.BookingDetailStatus.CANCELED) {
            Room room = bookingDetailUpdate.getRoom();
            room.setStatus(Room.RoomStatus.AVAILABLE);
            roomRepository.save(room);
        }

        return bookingDetailRepository.save(bookingDetailUpdate);
    }

    @Override
    public void deleteBookingDetail(Long bookingDetailId) {
        Optional<BookingDetail> bookingDetail = bookingDetailRepository.findById(bookingDetailId);
        if(bookingDetail.isEmpty()){
            throw new NotFoundException("BookingDetailNotFound","Not found BookingDetail with " + bookingDetailId);
        }
        bookingDetailRepository.deleteById(bookingDetailId);
    }

    private void setBookingDetail(BookingDetail bookingDetailUpdate, BookingDetail bookingDetail) {
        bookingDetailUpdate.setCheckIn(bookingDetail.getCheckIn());
        bookingDetailUpdate.setCheckOut(bookingDetail.getCheckOut());
        bookingDetailUpdate.setStatus(bookingDetail.getStatus());
        bookingDetailUpdate.setSpecialRequests(bookingDetail.getSpecialRequests());
        bookingDetailUpdate.setPrice(bookingDetail.getPrice());
        bookingDetailUpdate.setUpdatedAt(LocalDateTime.now());
    }
    public static Specification<BookingDetail> searchByKeyword(String keyword) {
        return (root, query, criteriaBuilder) -> {
            if (keyword == null || keyword.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            String likePattern = "%" + keyword + "%";
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("status").as(String.class)), likePattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("specialRequests").as(String.class)), likePattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("price").as(String.class)), likePattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("room").get("roomNumber").as(String.class)), likePattern)
            );
        };
    }
    public static Specification<BookingDetail> hasBookingId(Long bookingId) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("booking").get("id"), bookingId);
        };
    }
}
