package com.example.projectbackend.service;

import com.example.projectbackend.bean.request.RelProvisionBookingDetailRequest;
import com.example.projectbackend.bean.response.RelProvisionBookingDetailResponse;
import com.example.projectbackend.entity.BookingDetail;
import com.example.projectbackend.entity.Provision;
import com.example.projectbackend.entity.RelProvisionBookingDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RelProvisionBookingDetailService {
    public Page<RelProvisionBookingDetailResponse> getAllRel(Pageable pageable, String keyword);
    public RelProvisionBookingDetailResponse getDetailRel(Long relId);
    RelProvisionBookingDetail createRel(RelProvisionBookingDetailRequest relProvisionBookingDetailRequest);
    public RelProvisionBookingDetail updateRel(Long relId, RelProvisionBookingDetail relProvisionBookingDetail);
    public void deleteRel(Long relId);
}
