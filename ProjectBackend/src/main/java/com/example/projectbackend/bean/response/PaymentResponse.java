package com.example.projectbackend.bean.response;

import com.example.projectbackend.entity.Payment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {
    private Long paymentId;
    private String user;
    private Payment.PaymentStatus status;
    private Double paid;
    private LocalDate paymentDate;
    private String paymentReference;
}