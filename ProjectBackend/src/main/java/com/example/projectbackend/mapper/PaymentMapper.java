package com.example.projectbackend.mapper;

import com.example.projectbackend.bean.request.PaymentRequest;
import com.example.projectbackend.bean.response.PaymentResponse;
import com.example.projectbackend.entity.Payment;

public class PaymentMapper {
    public static PaymentResponse convertToResponse(Payment payment) {

        PaymentResponse paymentResponse = new PaymentResponse();
        paymentResponse.setPaymentId(payment.getPaymentId());
        paymentResponse.setStatus(payment.getStatus());
        return paymentResponse;
    }

    public static Payment convertFromRequest(PaymentRequest paymentRequest){
        Payment payment = new Payment();
        payment.setStatus(paymentRequest.getStatus());
        return payment;
    }
}
