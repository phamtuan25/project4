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

        // Chuyển đổi BookingDetails thành BookingDetailResponse
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


    // Phương thức chuyển đổi BookingDetailRequest thành BookingDetail
    private static BookingDetail convertFromRequestDetail(BookingDetailRequest detailRequest, Booking booking, RoomRepository roomRepository) {
        BookingDetail bookingDetail = new BookingDetail();
        bookingDetail.setCheckIn(detailRequest.getCheckIn());
        bookingDetail.setCheckOut(detailRequest.getCheckOut());
        bookingDetail.setSpecialRequests(detailRequest.getSpecialRequests());
        bookingDetail.setPrice(detailRequest.getPrice());
        bookingDetail.setBooking(booking);
        Room room = roomRepository.findById(detailRequest.getRoomId())
                .orElseThrow(() -> new RuntimeException("Room not found with ID: " + detailRequest.getRoomId()));
        bookingDetail.setRoom(room);

        return bookingDetail;
    }
}
