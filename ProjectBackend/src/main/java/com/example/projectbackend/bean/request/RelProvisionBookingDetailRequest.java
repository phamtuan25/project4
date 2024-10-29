package com.example.projectbackend.bean.request;

import com.example.projectbackend.entity.RelProvisionBookingDetail;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RelProvisionBookingDetailRequest {

    @NotNull(message = "Provision ID is required")
    private Long provisionId;

    @NotNull(message = "Booking Detail ID is required")
    private Long bookingDetailId;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private Double price;

    @NotNull(message = "Booking ID is required")
    private Long bookingId;

    @NotNull(message = "Status is required")
    private RelProvisionBookingDetail.RelProvisionBookingDetailStatus status;
}
