package com.example.projectbackend.service.impl;

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
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
        // Kiểm tra xem danh sách roomNumbers có rỗng không
        if (bookingRequest.getRoomNumbers() == null || bookingRequest.getRoomNumbers().isEmpty()) {
            throw new NotFoundException("RoomNumberMissing", "Room number list cannot be empty.");
        }

        // Tìm kiếm người dùng từ cơ sở dữ liệu
        User user = userRepository.findById(bookingRequest.getUser().getUserId())
                .orElseThrow(() -> new NotFoundException("UserNotFound", "User not found with ID: " + bookingRequest.getUser().getUserId()));

        // Tìm các phòng theo roomNumber
        List<Room> rooms = roomRepository.findAllByRoomNumberIn(bookingRequest.getRoomNumbers());

        if (rooms.isEmpty()) {
            throw new NotFoundException("RoomNotFound", "One or more rooms not found.");
        }

        // Tạo booking từ request
        Booking booking = new Booking();
        booking.setUser(user); // Gán người dùng đã tìm thấy
        booking.setCreatedAt(LocalDateTime.now());
        booking.setDeposit(bookingRequest.getDeposit());
        booking.setStatus(Booking.BookingStatus.PENDING);

        // Lưu booking để có bookingId
        Booking savedBooking = bookingRepository.save(booking);

        // Tạo BookingDetails cho từng phòng
        List<BookingDetail> bookingDetails = rooms.stream().map(room -> {
            BookingDetail bookingDetail = new BookingDetail();
            bookingDetail.setRoom(room);
            bookingDetail.setCheckIn(bookingRequest.getCheckIn());
            bookingDetail.setCheckOut(bookingRequest.getCheckOut());
            bookingDetail.setStatus(BookingDetail.BookingDetailStatus.PENDING);
            bookingDetail.setPrice(room.getPrice());
            bookingDetail.setBooking(savedBooking);
            return bookingDetail;
        }).collect(Collectors.toList());

        // Lưu BookingDetails
        bookingDetailRepository.saveAll(bookingDetails);

        // Cập nhật tổng số tiền
        double totalAmount = bookingRequest.getDeposit() + bookingDetails.stream().mapToDouble(BookingDetail::getPrice).sum();
        savedBooking.setTotalAmount(totalAmount);

        // Cập nhật booking với tổng số tiền
        return bookingRepository.save(savedBooking);
    }






    @Override
    public Booking updateBooking(Long bookingId, Booking booking) {
        Booking bookingUpdate = bookingRepository.findById(bookingId).orElse(null);
        if(bookingUpdate != null){
            setBooking(bookingUpdate, booking);
        }
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

    public void setBooking(Booking bookingUpdate, Booking bookingInput){
        bookingUpdate.setUser(bookingInput.getUser());
        bookingUpdate.setUpdatedAt(LocalDateTime.now());
    }
}
