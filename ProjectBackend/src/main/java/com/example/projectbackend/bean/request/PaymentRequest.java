package com.example.projectbackend.bean.request;

import com.example.projectbackend.entity.Payment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {
    private Long bookingId;
    private Payment.PaymentMethod paymentMethod;
    private Payment.PaymentStatus status;
}
