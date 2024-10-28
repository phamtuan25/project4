package com.example.projectbackend.service;

import com.example.projectbackend.bean.request.RelProvisionBookingDetailRequest;
import com.example.projectbackend.bean.response.RelProvisionBookingDetailResponse;
import com.example.projectbackend.entity.BookingDetail;
import com.example.projectbackend.entity.Provision;
import com.example.projectbackend.entity.RelProvisionBookingDetail;

import java.util.List;

public interface RelProvisionBookingDetailService {
    public List<RelProvisionBookingDetailResponse> getAllRel();
    public RelProvisionBookingDetailResponse getDetailRel(Long relId);
    RelProvisionBookingDetail createRel(RelProvisionBookingDetailRequest relProvisionBookingDetailRequest);
    public RelProvisionBookingDetail updateRel(Long relId, RelProvisionBookingDetail relProvisionBookingDetail);
    public void deleteRel(Long relId);
}
