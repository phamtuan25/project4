package com.example.projectbackend.bean.response;

import com.example.projectbackend.entity.RelProvisionBookingDetail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RelProvisionBookingDetailResponse {
    private Long relId;
    private Long provisionId;
    private Long bookingDetailId;
    private String roomNumber;
    private String provisionName;
    private Double price;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private RelProvisionBookingDetail.RelProvisionBookingDetailStatus status;
}
