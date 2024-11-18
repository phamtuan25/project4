package com.example.projectbackend.service;

import com.example.projectbackend.bean.request.BookingDetailRequest;
import com.example.projectbackend.bean.response.BookingDetailResponse;
import com.example.projectbackend.entity.BookingDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public interface BookingDetailService {
    public Page<BookingDetailResponse> getAllBookingDetails(Pageable pageable, String keyword, Long bookingId);
    public BookingDetailResponse getDetailBookingDetail(Long bookingDetailId);
    public BookingDetail createBookingDetail(BookingDetailRequest bookingDetailRequest, Long bookingId);
    public BookingDetail updateBookingDetail(Long bookingDetailId, BookingDetail bookingDetail);
    public void deleteBookingDetail(Long bookingDetailId);

}
