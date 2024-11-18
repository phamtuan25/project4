package com.example.projectbackend.mapper;

import com.example.projectbackend.bean.request.RelProvisionBookingDetailRequest;
import com.example.projectbackend.bean.response.RelProvisionBookingDetailResponse;
import com.example.projectbackend.entity.BookingDetail;
import com.example.projectbackend.entity.Provision;
import com.example.projectbackend.entity.RelProvisionBookingDetail;

public class RelProvisionBookingDetailMapper {
    public static RelProvisionBookingDetailResponse convertToResponse(RelProvisionBookingDetail relProvisionBookingDetail){
        RelProvisionBookingDetailResponse relProvisionBookingDetailResponse = new RelProvisionBookingDetailResponse();
        relProvisionBookingDetailResponse.setRelId(relProvisionBookingDetail.getRelId());
        relProvisionBookingDetailResponse.setProvisionId(relProvisionBookingDetail.getProvision().getProvisionId());
        relProvisionBookingDetailResponse.setBookingDetailId(relProvisionBookingDetail.getBookingDetail().getBookingDetailId());
        relProvisionBookingDetailResponse.setRoomNumber(relProvisionBookingDetail.getBookingDetail().getRoom().getRoomNumber());
        relProvisionBookingDetailResponse.setProvisionName(relProvisionBookingDetail.getProvision().getProvisionName());
        relProvisionBookingDetailResponse.setStatus(relProvisionBookingDetail.getStatus());
        relProvisionBookingDetailResponse.setPrice(relProvisionBookingDetail.getPrice());
        relProvisionBookingDetailResponse.setCreatedAt(relProvisionBookingDetail.getCreatedAt());
        relProvisionBookingDetailResponse.setUpdatedAt(relProvisionBookingDetail.getUpdatedAt());
        return relProvisionBookingDetailResponse;
    }

    public static RelProvisionBookingDetail convertFromRequest(RelProvisionBookingDetailRequest relProvisionBookingDetailRequest){
        RelProvisionBookingDetail relProvisionBookingDetail = new RelProvisionBookingDetail();
        relProvisionBookingDetail.setStatus(relProvisionBookingDetailRequest.getStatus());
        return relProvisionBookingDetail;
    }
}
