package com.example.projectbackend.bean.request;

import com.example.projectbackend.entity.RelProvisionBookingDetail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RelProvisionBookingDetailRequest {
    private Long provisionId;
    private Long bookingDetailId;
    private Double price;
    private Long bookingId;
    private RelProvisionBookingDetail.RelProvisionBookingDetailStatus status;
}
