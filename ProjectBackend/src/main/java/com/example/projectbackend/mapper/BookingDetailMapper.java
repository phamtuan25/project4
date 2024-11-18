package com.example.projectbackend.mapper;

import com.example.projectbackend.bean.request.BookingDetailRequest;
import com.example.projectbackend.bean.response.BookingDetailResponse;
import com.example.projectbackend.bean.response.RelProvisionBookingDetailResponse;
import com.example.projectbackend.entity.BookingDetail;
import com.example.projectbackend.entity.Provision;
import com.example.projectbackend.entity.RelProvisionBookingDetail;
import com.example.projectbackend.repository.ProvisionRepository;

import java.util.List;
import java.util.stream.Collectors;

public class BookingDetailMapper {

    public static BookingDetailResponse convertToResponse(BookingDetail bookingDetail) {
        BookingDetailResponse bookingDetailResponse = new BookingDetailResponse();
        bookingDetailResponse.setBookingDetailId(bookingDetail.getBookingDetailId());
        bookingDetailResponse.setBookingId(bookingDetail.getBooking().getBookingId());
        bookingDetailResponse.setRoomId(bookingDetail.getRoom().getRoomId());
        bookingDetailResponse.setCheckIn(bookingDetail.getCheckIn());
        bookingDetailResponse.setCheckOut(bookingDetail.getCheckOut());
        bookingDetailResponse.setCreatedAt(bookingDetail.getCreatedAt());
        bookingDetailResponse.setUpdatedAt(bookingDetail.getUpdatedAt());
        bookingDetailResponse.setStatus(bookingDetail.getStatus());
        bookingDetailResponse.setSpecialRequests(bookingDetail.getSpecialRequests());
        bookingDetailResponse.setPrice(bookingDetail.getPrice());
        bookingDetailResponse.setRoomNumber(bookingDetail.getRoom().getRoomNumber());
        List<RelProvisionBookingDetailResponse> provisionBookingResponse = bookingDetail.getRelProvisionBookingDetails().stream()
                .map(RelProvisionBookingDetailMapper::convertToResponse)
                .collect(Collectors.toList());
        bookingDetailResponse.setProvisionBookingResponse(provisionBookingResponse);
        if (bookingDetail.getRelProvisionBookingDetails() != null) {
            List<Long> provisionIds = bookingDetail.getRelProvisionBookingDetails().stream()
                    .map(rel -> rel.getProvision().getProvisionId())
                    .collect(Collectors.toList());
            bookingDetailResponse.setProvisionIds(provisionIds);
        }

        return bookingDetailResponse;
    }

    public static BookingDetail convertFromRequest(BookingDetailRequest bookingDetailRequest, ProvisionRepository provisionRepository) {
        BookingDetail bookingDetail = new BookingDetail();
        bookingDetail.setCheckIn(bookingDetailRequest.getCheckIn());
        bookingDetail.setCheckOut(bookingDetailRequest.getCheckOut());
        bookingDetail.setStatus(BookingDetail.BookingDetailStatus.PENDING);
        bookingDetail.setSpecialRequests(bookingDetailRequest.getSpecialRequests());
        bookingDetail.setPrice(bookingDetailRequest.getPrice());

        if (bookingDetailRequest.getProvisionIds() != null && !bookingDetailRequest.getProvisionIds().isEmpty()) {
            List<Provision> provisions = provisionRepository.findAllByProvisionIdIn(bookingDetailRequest.getProvisionIds());
            List<RelProvisionBookingDetail> relProvisionBookingDetails = bookingDetail.getRelProvisionBookingDetails();
            for (Provision provision : provisions) {
                RelProvisionBookingDetail relProvisionBookingDetail = new RelProvisionBookingDetail();
                relProvisionBookingDetail.setProvision(provision);
                relProvisionBookingDetail.setBookingDetail(bookingDetail);
                relProvisionBookingDetail.setPrice(provision.getPrice().doubleValue());

                relProvisionBookingDetails.add(relProvisionBookingDetail);
            }
            bookingDetail.setRelProvisionBookingDetails(relProvisionBookingDetails);
        }

        return bookingDetail;
    }
}
