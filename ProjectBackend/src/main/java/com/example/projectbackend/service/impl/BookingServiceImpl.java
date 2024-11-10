package com.example.projectbackend.service.impl;

import com.example.projectbackend.bean.request.BookingDetailRequest;
import com.example.projectbackend.bean.request.BookingRequest;
import com.example.projectbackend.bean.response.BookingResponse;
import com.example.projectbackend.entity.*;
import com.example.projectbackend.exception.EmptyListException;
import com.example.projectbackend.exception.NotFoundException;
import com.example.projectbackend.mapper.BookingMapper;
import com.example.projectbackend.repository.BookingDetailRepository;
import com.example.projectbackend.repository.BookingRepository;
import com.example.projectbackend.repository.RoomRepository;
import com.example.projectbackend.repository.UserRepository;
import com.example.projectbackend.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
    public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final BookingDetailRepository bookingDetailRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    @Override
    public Page<BookingResponse> getAllBookings(@PageableDefault(size = 10, page = 0) Pageable pageable,
                                                @RequestParam(required = false) String keyword) {
        Specification<Booking> spec = searchByKeyword(keyword);
        Page<Booking> bookingsPage = bookingRepository.findAll(spec, pageable);
        return bookingsPage.map(booking -> {
            BookingResponse bookingResponse = BookingMapper.convertToResponse(booking);
            return bookingResponse;
        });
    }


    @Override
    public BookingResponse getDetailBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElse(null);
        if(Objects.isNull(booking)){
            throw new NotFoundException("BookingNotFound","Not found Booking with " + bookingId);
        }
        BookingResponse bookResponse = BookingMapper.convertToResponse(booking);
        return bookResponse;
    }

    @Override
    public Booking createBooking(BookingRequest bookingRequest) {
        // Kiểm tra xem booking detail có rỗng không
        List<BookingDetailRequest> bookingDetailRequests = bookingRequest.getBookingDetailRequests();

        // Tìm kiếm người dùng từ cơ sở dữ liệu
        User user = userRepository.findById(bookingRequest.getUser().getUserId())
                .orElseThrow(() -> new NotFoundException("UserNotFound", "User not found with ID: " + bookingRequest.getUser().getUserId()));

        if (bookingDetailRequests == null || bookingDetailRequests.isEmpty()) {
            throw new NotFoundException("BookingDetailEmpty", "Booking Detail cannot be empty.");
        }

        // Lấy danh sách roomId từ bookingDetailRequests
        List<Long> roomIds = bookingDetailRequests.stream()
                .map(BookingDetailRequest::getRoomId)
                .collect(Collectors.toList());

        // Lấy danh sách phòng từ cơ sở dữ liệu bằng roomId
        List<Room> rooms = roomRepository.findAllById(roomIds);
        if (rooms.isEmpty()) {
            throw new NotFoundException("RoomNotFound", "One or more rooms not found.");
        }

        // Tạo booking từ request
        Booking booking = BookingMapper.convertFromRequest(bookingRequest, roomRepository);
        booking.setUser(user); // Gán người dùng đã tìm thấy
        booking.setCreatedAt(LocalDateTime.now());
        booking.setStatus(Booking.BookingStatus.PENDING);

        // Lưu booking để có bookingId
        Booking savedBooking = bookingRepository.save(booking);

        // Cập nhật trạng thái phòng và BookingDetails
        List<BookingDetail> bookingDetails = new ArrayList<>(booking.getBookingDetails()); // Sao chép danh sách để tránh ConcurrentModificationException

        for (BookingDetail bookingDetail : bookingDetails) {
            // Tìm phòng tương ứng với booking detail
            Room room = rooms.stream()
                    .filter(r -> r.getRoomId().equals(bookingDetail.getRoom().getRoomId()))
                    .findFirst()
                    .orElseThrow(() -> new NotFoundException("RoomNotFound", "Room not found with ID: " + bookingDetail.getRoom().getRoomId()));

            // Kiểm tra trạng thái phòng
            if (room.getStatus() != Room.RoomStatus.AVAILABLE) {
                throw new NotFoundException("RoomNotAvailable", "Room with ID " + room.getRoomId() + " is not available.");
            }

            // Cập nhật trạng thái phòng thành BOOKED
            room.setStatus(Room.RoomStatus.BOOKED);
            roomRepository.save(room); // Lưu phòng sau khi thay đổi trạng thái

            // Cập nhật thông tin booking detail
            bookingDetail.setRoom(room);
            bookingDetail.setBooking(savedBooking); // Gán booking cho chi tiết đặt phòng
            bookingDetail.setCreatedAt(savedBooking.getCreatedAt()); // Gán createdAt của booking cho bookingDetail
        }

        // Lưu tất cả các booking details
        bookingDetailRepository.saveAll(bookingDetails);

        // Cập nhật tổng số tiền
        double totalAmount = bookingRequest.getDeposit() + bookingDetails.stream().mapToDouble(BookingDetail::getPrice).sum();
        savedBooking.setTotalAmount(Double.valueOf(totalAmount)); // Đảm bảo tổng số tiền là BigDecimal

        // Cập nhật booking với tổng số tiền
        return bookingRepository.save(savedBooking);
    }



    @Override
    public Booking updateBooking(Long bookingId, Booking booking) {
        // Tìm kiếm booking theo bookingId
        Booking bookingUpdate = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("BookingNotFound", "Booking not found with ID: " + bookingId));

        // Cập nhật thông tin booking
        setBooking(bookingUpdate, booking);

        // Lưu booking đã cập nhật vào cơ sở dữ liệu
        return bookingRepository.save(bookingUpdate);
    }

    @Override
    public void deleteBooking(Long bookingId) {
        Optional<Booking> booking = bookingRepository.findById(bookingId);

        if (booking.isEmpty()) {
            throw new NotFoundException("BookingNotFound","Booking with ID " + bookingId + " not found.");
        }
        bookingRepository.deleteById(bookingId);
    }

    private void setBooking(Booking bookingUpdate, Booking booking) {
        bookingUpdate.setStatus(booking.getStatus());
        bookingUpdate.setDeposit(booking.getDeposit());
        bookingUpdate.setTotalAmount(booking.getTotalAmount());
         bookingUpdate.setUpdatedAt(LocalDateTime.now());
    }
    public static Specification<Booking> searchByKeyword(String keyword) {
        return (root, query, criteriaBuilder) -> {
            if (keyword == null || keyword.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            String likePattern = "%" + keyword + "%";
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("status").as(String.class)), likePattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("deposit").as(String.class)), likePattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("totalAmount").as(String.class)), likePattern)
            );
        };
    }
}
