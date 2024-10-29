package com.example.projectbackend.mapper;

import com.example.projectbackend.bean.request.BookingRequest;
import com.example.projectbackend.bean.response.BookingResponse;
import com.example.projectbackend.entity.Booking;

public class BookingMapper {
    public static BookingResponse convertToResponse(Booking booking){
        BookingResponse bookingResponse = new BookingResponse();
        bookingResponse.setBookingId(booking.getBookingId());
        bookingResponse.setUserResponse(UserMapper.convertToResponse(booking.getUser()));
        bookingResponse.setCreatedAt(booking.getCreatedAt());
        bookingResponse.setUpdatedAt(booking.getUpdatedAt());
        bookingResponse.setStatus(booking.getStatus());
        bookingResponse.setDeposit(booking.getDeposit());
        return bookingResponse;
    }

    public static Booking convertFromRequest(BookingRequest bookingRequest){
        Booking booking = new Booking();
        booking.setUser(bookingRequest.getUser());
        booking.setStatus(bookingRequest.getStatus());
        booking.setDeposit(bookingRequest.getDeposit());
        return booking;
    }
}
