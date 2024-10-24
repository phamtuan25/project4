package com.example.projectbackend.service;

import com.example.projectbackend.bean.request.BookingRequest;
import com.example.projectbackend.bean.response.BookingResponse;
import com.example.projectbackend.entity.Booking;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BookingService {
    public List<BookingResponse> getAllBookings();
    public BookingResponse getDetailBooking(Long bookingId);
    public Booking createBooking(BookingRequest bookingRequest);
    public Booking updateBooking(Long bookingId, Booking booking);
    public void deleteBooking(Long bookingId);
}
