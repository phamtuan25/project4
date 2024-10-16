package com.example.projectbackend.mapper;

import com.example.projectbackend.bean.request.BookingRequest;
import com.example.projectbackend.bean.response.BookingResponse;
import com.example.projectbackend.entity.Booking;

public class BookingMapper {
    public static BookingResponse convertToResponseInfo(Booking booking){
        BookingResponse bookingResponse = new BookingResponse();
        bookingResponse.setBookingId(booking.getBookingId());
        bookingResponse.setUser(booking.getUser());
        bookingResponse.setCreatedAt(booking.getCreatedAt());
        bookingResponse.setUpdatedAt(booking.getUpdatedAt());
        bookingResponse.setBookingDetails(booking.getBookingDetails());
        return bookingResponse;
    }

    public static Booking convertFromRequest(BookingRequest bookingRequest){
        Booking booking = new Booking();
        booking.setUser(bookingRequest.getUser());
        return booking;
    }
}
