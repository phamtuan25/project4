package com.example.projectbackend.service;

import com.example.projectbackend.bean.request.BookingDetailRequest;
import com.example.projectbackend.bean.response.BookingDetailResponse;
import com.example.projectbackend.entity.BookingDetail;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BookingDetailService {
    public List<BookingDetailResponse> getAllBookingDetails();
    public BookingDetailResponse getDetailBookingDetail(Long bookingDetailId);
    public BookingDetail createBookingDetail(BookingDetailRequest bookingDetailRequest, Long bookingId);
    public BookingDetail updateBookingDetail(Long bookingDetailId, BookingDetail bookingDetail);
    public void deleteBookingDetail(Long bookingDetailId);

}
