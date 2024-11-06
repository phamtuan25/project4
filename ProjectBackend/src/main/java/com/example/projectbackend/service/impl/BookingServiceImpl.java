package com.example.projectbackend.service.impl;

import com.example.projectbackend.bean.request.BookingDetailRequest;
import com.example.projectbackend.bean.request.BookingRequest;
import com.example.projectbackend.bean.response.BookingResponse;
import com.example.projectbackend.entity.Booking;
import com.example.projectbackend.entity.BookingDetail;
import com.example.projectbackend.entity.Room;
import com.example.projectbackend.entity.User;
import com.example.projectbackend.exception.EmptyListException;
import com.example.projectbackend.exception.NotFoundException;
import com.example.projectbackend.mapper.BookingMapper;
import com.example.projectbackend.repository.BookingDetailRepository;
import com.example.projectbackend.repository.BookingRepository;
import com.example.projectbackend.repository.RoomRepository;
import com.example.projectbackend.repository.UserRepository;
import com.example.projectbackend.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
    public List<BookingResponse> getAllBookings() {
        if(bookingRepository.findAll().isEmpty()) {
            throw new EmptyListException("EmptyBooking","This list Booking is empty");
        }
        return bookingRepository.findAll().stream().map(BookingMapper::convertToResponse).collect(Collectors.toList());
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

        // Cập nhật BookingDetails với booking đã lưu và room đã tìm thấy
        List<BookingDetail> bookingDetails = new ArrayList<>(booking.getBookingDetails()); // Sao chép danh sách để tránh ConcurrentModificationException
        for (BookingDetail bookingDetail : bookingDetails) {
            Room room = rooms.stream()
                    .filter(r -> r.getRoomId().equals(bookingDetail.getRoom().getRoomId()))
                    .findFirst()
                    .orElseThrow(() -> new NotFoundException("RoomNotFound", "Room not found with ID: " + bookingDetail.getRoom().getRoomId()));
            bookingDetail.setRoom(room);
            bookingDetail.setBooking(savedBooking); // Gán booking cho chi tiết đặt phòng
            bookingDetail.setCreatedAt(savedBooking.getCreatedAt()); // Gán createdAt của booking cho bookingDetail
        }

        // Lưu BookingDetails
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
        bookingUpdate.setUser(booking.getUser());
        bookingUpdate.setStatus(booking.getStatus());
        bookingUpdate.setDeposit(booking.getDeposit());
        bookingUpdate.setTotalAmount(booking.getTotalAmount());
         bookingUpdate.setUpdatedAt(LocalDateTime.now());
    }
}
