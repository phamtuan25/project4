package com.example.projectbackend.bean.response;

import com.example.projectbackend.entity.Payment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {
    private Long paymentId;
    private Long bookingId;
    private Payment.PaymentStatus status;
}