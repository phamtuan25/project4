package com.example.projectbackend.mapper;

import com.example.projectbackend.bean.request.BookingDetailRequest;
import com.example.projectbackend.bean.response.BookingDetailResponse;
import com.example.projectbackend.entity.Booking;
import com.example.projectbackend.entity.BookingDetail;
import com.example.projectbackend.entity.Room;
import com.example.projectbackend.repository.RoomRepository;

public class BookingDetailMapper {
    public  static BookingDetailResponse convertToResponse(BookingDetail bookingDetail){
        BookingDetailResponse bookingDetailResponse = new BookingDetailResponse();
        bookingDetailResponse.setBookingDetailId(bookingDetail.getBookingDetailId());
        bookingDetailResponse.setBookingId(bookingDetail.getBooking().getBookingId());
        bookingDetailResponse.setRoomId(bookingDetail.getRoom().getRoomId());
        bookingDetailResponse.setCheckIn(bookingDetail.getCheckIn());
        bookingDetailResponse.setCheckOut(bookingDetail.getCheckOut());
        bookingDetailResponse.setCreatedAt(bookingDetail.getCreatedAt());
        bookingDetailResponse.setUpdatedAt(bookingDetail.getUpdatedAt());
        bookingDetailResponse.setStatus(bookingDetail.getStatus());
        bookingDetailResponse.setSpecialRequests(bookingDetailResponse.getSpecialRequests());
        bookingDetailResponse.setPrice(bookingDetailResponse.getPrice());
        return bookingDetailResponse;
    }

    public static BookingDetail convertFromRequest(BookingDetailRequest bookingDetailRequest) {
        BookingDetail bookingDetail = new BookingDetail();
        bookingDetail.setCheckIn(bookingDetailRequest.getCheckIn());
        bookingDetail.setCheckOut(bookingDetailRequest.getCheckOut());
        bookingDetail.setStatus(BookingDetail.BookingDetailStatus.PENDING);
        bookingDetail.setSpecialRequests(bookingDetailRequest.getSpecialRequests());
        return bookingDetail;
    }
}
