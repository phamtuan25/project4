package com.example.projectbackend.mapper;

import com.example.projectbackend.bean.request.RelProvisionBookingDetailRequest;
import com.example.projectbackend.bean.response.RelProvisionBookingDetailResponse;
import com.example.projectbackend.entity.BookingDetail;
import com.example.projectbackend.entity.Provision;
import com.example.projectbackend.entity.RelProvisionBookingDetail;

public class RelProvisionBookingDetailMapper {
    public static RelProvisionBookingDetailResponse convertToResponse(RelProvisionBookingDetail relProvisionBookingDetail){
        RelProvisionBookingDetailResponse relProvisionBookingDetailResponse = new RelProvisionBookingDetailResponse();
        relProvisionBookingDetailResponse.setProvisionId(relProvisionBookingDetailResponse.getProvisionId());
        relProvisionBookingDetailResponse.setBookingDetailId(relProvisionBookingDetailResponse.getBookingDetailId());
        relProvisionBookingDetailResponse.setStatus(relProvisionBookingDetailResponse.getStatus());
        relProvisionBookingDetailResponse.setPrice(relProvisionBookingDetailResponse.getPrice());
        return relProvisionBookingDetailResponse;
    }

    public static RelProvisionBookingDetail convertFromRequest(RelProvisionBookingDetailRequest relProvisionBookingDetailRequest){
        RelProvisionBookingDetail relProvisionBookingDetail = new RelProvisionBookingDetail();
        relProvisionBookingDetail.setStatus(relProvisionBookingDetailRequest.getStatus());
        relProvisionBookingDetail.setPrice(relProvisionBookingDetailRequest.getPrice());
        return relProvisionBookingDetail;
    }
}
