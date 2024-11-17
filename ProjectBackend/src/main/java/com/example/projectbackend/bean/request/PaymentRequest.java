package com.example.projectbackend.bean.request;

import com.example.projectbackend.entity.Payment;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {

    @NotNull(message = "Booking ID is required")
    private Long bookingId;


    @NotNull(message = "Payment status is required")
    private Payment.PaymentStatus status;
}
