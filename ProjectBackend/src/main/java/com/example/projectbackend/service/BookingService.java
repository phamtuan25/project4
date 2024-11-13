package com.example.projectbackend.service;

import com.example.projectbackend.bean.request.BookingRequest;
import com.example.projectbackend.bean.response.BookingResponse;
import com.example.projectbackend.entity.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BookingService {
    public Page<BookingResponse> getAllBookings(Pageable pageable, String keyword);
    public List<BookingResponse> getBookingsByUserId(Long userId);
    public Booking createBooking(BookingRequest bookingRequest);
    public Booking updateBooking(Long bookingId, Booking booking);
    public void deleteBooking(Long bookingId);
}
