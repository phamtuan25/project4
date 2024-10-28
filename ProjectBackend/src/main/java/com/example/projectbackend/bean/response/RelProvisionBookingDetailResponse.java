package com.example.projectbackend.bean.response;

import com.example.projectbackend.entity.RelProvisionBookingDetail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RelProvisionBookingDetailResponse {
    private Long provisionId;
    private Long bookingDetailId;
    private Long bookingId;
    private Double price;
    private LocalDateTime createdAt;
    private RelProvisionBookingDetail.RelProvisionBookingDetailStatus status;
}
