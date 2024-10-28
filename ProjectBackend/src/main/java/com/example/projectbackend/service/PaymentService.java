package com.example.projectbackend.service;

import com.example.projectbackend.bean.request.PaymentRequest;
import com.example.projectbackend.bean.response.PaymentResponse;
import com.example.projectbackend.entity.Payment;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PaymentService {
    public List<PaymentResponse> getAllPayments();
    public PaymentResponse getDetailPayment(Long paymentId);
    public Payment createPayment(PaymentRequest paymentRequest);
    public Payment updatePayment(Long paymentId, Payment payment);
    public void deletePayment(Long paymentId);
}
