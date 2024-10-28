package com.example.projectbackend.controller;

import com.example.projectbackend.bean.request.BookingDetailRequest;
import com.example.projectbackend.bean.response.BookingDetailResponse;
import com.example.projectbackend.entity.Booking;
import com.example.projectbackend.entity.BookingDetail;
import com.example.projectbackend.entity.Room;
import com.example.projectbackend.service.BookingDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bookingdetails")
public class BookingDetailController {
    private final BookingDetailService bookingDetailService;

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'EMPLOYEE')")
    @GetMapping
    public List<BookingDetailResponse> getAllBookingDetails(){
        return bookingDetailService.getAllBookingDetails();
    }

    @GetMapping("/bookingDetailId")
    public BookingDetailResponse getBookingDetailById(@PathVariable Long bookingDetailId){
        return bookingDetailService.getDetailBookingDetail(bookingDetailId);
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'EMPLOYEE')")
    @PostMapping("/{bookingId}")
    public BookingDetail createBookingDetail(@PathVariable Long bookingId, @RequestBody BookingDetailRequest bookingDetailRequest){
        return bookingDetailService.createBookingDetail(bookingDetailRequest, bookingId);
    }
    @PostMapping("/bookingDetailId")
    public BookingDetail updateBookingDetail(@PathVariable Long bookingDetailId, @RequestBody BookingDetail bookingDetail){
        return bookingDetailService.updateBookingDetail(bookingDetailId, bookingDetail);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'EMPLOYEE')")
    @DeleteMapping("/bookingDetailId")
    public void deleteBookingDetail(@PathVariable Long bookingDetailId){
        bookingDetailService.deleteBookingDetail(bookingDetailId);
    }
}