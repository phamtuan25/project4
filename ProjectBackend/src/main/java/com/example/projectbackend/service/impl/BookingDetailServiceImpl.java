package com.example.projectbackend.service.impl;

import com.example.projectbackend.bean.request.BookingDetailRequest;
import com.example.projectbackend.bean.response.BookingDetailResponse;
import com.example.projectbackend.entity.Booking;
import com.example.projectbackend.entity.BookingDetail;
import com.example.projectbackend.entity.Room;
import com.example.projectbackend.exception.NotFoundException;
import com.example.projectbackend.mapper.BookingDetailMapper;
import com.example.projectbackend.repository.BookingDetailRepository;
import com.example.projectbackend.repository.BookingRepository;
import com.example.projectbackend.repository.ProvisionRepository;
import com.example.projectbackend.repository.RoomRepository;
import com.example.projectbackend.service.BookingDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

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
        // Chuyển đổi từ request thành entity
        BookingDetail bookingDetail = BookingDetailMapper.convertFromRequest(bookingDetailRequest);

        // Tìm booking
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("BookingNotFound", "Booking không tìm thấy với ID: " + bookingId));
        bookingDetail.setBooking(booking);

        // Tìm phòng
        Room room = roomRepository.findById(bookingDetailRequest.getRoomId())
                .orElseThrow(() -> new NotFoundException("RoomNotFound", "Phòng không tìm thấy với ID: " + bookingDetailRequest.getRoomId()));
        bookingDetail.setRoom(room);

        // Kiểm tra nếu phòng có sẵn trong khoảng thời gian đặt
        boolean isRoomAvailable = checkRoomAvailability(room, bookingDetailRequest.getCheckIn(), bookingDetailRequest.getCheckOut());
        if (!isRoomAvailable) {
            throw new IllegalArgumentException("Phòng đã được đặt trong khoảng thời gian bạn yêu cầu");
        }

        // Đánh dấu phòng là đã được đặt
        room.setStatus(Room.RoomStatus.BOOKED);
        roomRepository.save(room);

        // Set the status to PENDING when creating the BookingDetail
        bookingDetail.setStatus(BookingDetail.BookingDetailStatus.PENDING); // Set BookingDetail status to PENDING

        return bookingDetailRepository.save(bookingDetail);
    }


    private boolean checkRoomAvailability(Room room, LocalDateTime checkIn, LocalDateTime checkOut) {
        // Kiểm tra nếu phòng đã có booking trong khoảng thời gian này
        List<BookingDetail> overlappingBookings = bookingDetailRepository.findOverlappingBookings(room.getRoomId(), checkIn, checkOut);
        return overlappingBookings.isEmpty();
    }



    @Override
    public BookingDetail updateBookingDetail(Long bookingDetailId, BookingDetail bookingDetail) {
        // Tìm kiếm BookingDetail theo ID
        BookingDetail bookingDetailUpdate = bookingDetailRepository.findById(bookingDetailId)
                .orElseThrow(() -> new NotFoundException("BookingDetailNotFound", "Booking Detail not found with ID: " + bookingDetailId));

        // Set các giá trị được cập nhật vào BookingDetail hiện tại
        setBookingDetail(bookingDetailUpdate, bookingDetail);

        // Kiểm tra nếu trạng thái được cập nhật thành CONFIRMED
        if (bookingDetailUpdate.getStatus() == BookingDetail.BookingDetailStatus.CONFIRMED) {
            // Nếu trạng thái là CONFIRMED, cập nhật trạng thái phòng thành AVAILABLE
            Room room = bookingDetailUpdate.getRoom();

            // Kiểm tra nếu phòng không phải là trạng thái AVAILABLE (đảm bảo không thay đổi trạng thái của phòng đã có sẵn)
            if (room.getStatus() != Room.RoomStatus.AVAILABLE) {
                room.setStatus(Room.RoomStatus.AVAILABLE); // Đặt trạng thái phòng thành AVAILABLE
                roomRepository.save(room);  // Lưu trạng thái phòng mới
            }
        }

        // Kiểm tra nếu trạng thái được cập nhật thành CANCELED
        if (bookingDetailUpdate.getStatus() == BookingDetail.BookingDetailStatus.CANCELED) {
            // Nếu trạng thái là CANCELED, cập nhật trạng thái của phòng thành AVAILABLE
            Room room = bookingDetailUpdate.getRoom();
            room.setStatus(Room.RoomStatus.AVAILABLE);
            roomRepository.save(room);  // Persist the room status change
        }

        // Lưu BookingDetail đã được cập nhật
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
