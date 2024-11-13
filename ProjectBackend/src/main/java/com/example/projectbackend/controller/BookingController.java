package com.example.projectbackend.controller;

import com.example.projectbackend.bean.request.BookingRequest;
import com.example.projectbackend.bean.response.BookingResponse;
import com.example.projectbackend.entity.Booking;
import com.example.projectbackend.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bookings")
public class BookingController {
    private final BookingService bookingService;
    @GetMapping
    public Page<BookingResponse> getAllBookings(Pageable pageable, @RequestParam(required = false) String keyword) {
        return bookingService.getAllBookings(pageable,keyword);
    }

    @GetMapping("/{userId}")
    public List<BookingResponse> getBookingByUser(@PathVariable Long userId) {
        return bookingService.getBookingsByUserId(userId);
    }

    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE', 'MANAGER')")
    @PostMapping
    public Booking createBooking(@Valid @RequestBody BookingRequest bookingRequest) {
        return bookingService.createBooking(bookingRequest);
    }

    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE', 'MANAGER')")
    @PutMapping("/{bookingId}")
    public Booking updateBooking(@PathVariable Long bookingId, @RequestBody Booking booking) {
        return bookingService.updateBooking(bookingId, booking);
    }

    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE', 'MANAGER')")
    @DeleteMapping("/{bookingId}")
    public void deleteBooking(@PathVariable Long bookingId) {
        bookingService.deleteBooking(bookingId);
    }
}
