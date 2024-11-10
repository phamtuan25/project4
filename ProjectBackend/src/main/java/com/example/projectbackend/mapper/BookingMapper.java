package com.example.projectbackend.mapper;

import com.example.projectbackend.bean.request.BookingDetailRequest;
import com.example.projectbackend.bean.request.BookingRequest;
import com.example.projectbackend.bean.response.BookingDetailResponse;
import com.example.projectbackend.bean.response.BookingResponse;
import com.example.projectbackend.entity.Booking;
import com.example.projectbackend.entity.BookingDetail;
import com.example.projectbackend.entity.Room;
import com.example.projectbackend.repository.RoomRepository;

import java.util.List;
import java.util.stream.Collectors;

public class BookingMapper {
    public static BookingResponse convertToResponse(Booking booking) {
        BookingResponse bookingResponse = new BookingResponse();
        bookingResponse.setBookingId(booking.getBookingId());
        bookingResponse.setUserBookingResponse(UserBookingMapper.convertToResponse(booking.getUser()));

        List<BookingDetailResponse> detailResponses = booking.getBookingDetails().stream()
                .map(BookingDetailMapper::convertToResponse)
                .collect(Collectors.toList());

        bookingResponse.setBookingDetailResponses(detailResponses);
        bookingResponse.setCreatedAt(booking.getCreatedAt());
        bookingResponse.setUpdatedAt(booking.getUpdatedAt());
        bookingResponse.setStatus(booking.getStatus());
        bookingResponse.setDeposit(booking.getDeposit());
        bookingResponse.setTotalAmount(booking.getTotalAmount());

        return bookingResponse;
    }

    public static Booking convertFromRequest(BookingRequest bookingRequest, RoomRepository roomRepository) {
        if (bookingRequest == null) {
            throw new IllegalArgumentException("BookingRequest cannot be null");
        }

        Booking booking = new Booking();

        // Kiểm tra nếu user là null
        if (bookingRequest.getUser() == null) {
            throw new IllegalArgumentException("User in booking request cannot be null");
        }

        booking.setUser(bookingRequest.getUser());
        booking.setStatus(bookingRequest.getStatus());
        booking.setDeposit(bookingRequest.getDeposit());

        // Kiểm tra nếu danh sách BookingDetails là null
        if (bookingRequest.getBookingDetailRequests() == null) {
            throw new IllegalArgumentException("BookingDetailRequests cannot be null");
        }

        // Chuyển đổi các BookingDetailRequest thành BookingDetail entity
        List<BookingDetail> bookingDetails = bookingRequest.getBookingDetailRequests().stream()
                .map(detailRequest -> convertFromRequestDetail(detailRequest, booking, roomRepository))
                .collect(Collectors.toList());

        booking.setBookingDetails(bookingDetails);

        // Tính toán totalAmount sau khi đã có bookingDetails
        booking.calculateTotalAmount();

        return booking;
    }

    private static BookingDetail convertFromRequestDetail(BookingDetailRequest detailRequest, Booking booking, RoomRepository roomRepository) {
        BookingDetail bookingDetail = new BookingDetail();
        bookingDetail.setCheckIn(detailRequest.getCheckIn());
        bookingDetail.setCheckOut(detailRequest.getCheckOut());
        bookingDetail.setStatus(detailRequest.getStatus());
        bookingDetail.setSpecialRequests(detailRequest.getSpecialRequests());
        bookingDetail.setPrice(detailRequest.getPrice());
        bookingDetail.setBooking(booking);
        Room room = roomRepository.findById(detailRequest.getRoomId())
                .orElseThrow(() -> new RuntimeException("Room not found with ID: " + detailRequest.getRoomId()));
        bookingDetail.setRoom(room);

        return bookingDetail;
    }
}
