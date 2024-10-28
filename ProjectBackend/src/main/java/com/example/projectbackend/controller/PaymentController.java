package com.example.projectbackend.controller;

import com.example.projectbackend.bean.request.PaymentRequest;
import com.example.projectbackend.bean.response.PaymentResponse;
import com.example.projectbackend.bean.response.RoomResponse;
import com.example.projectbackend.entity.Payment;
import com.example.projectbackend.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
public class PaymentController {
    private final PaymentService paymentService;

    @GetMapping
    public List<PaymentResponse> getAllPayments() {
        return paymentService.getAllPayments();
    }

    @GetMapping("/{paymentId}")
    public PaymentResponse getDetailPayment(@PathVariable Long paymentId) {
        return paymentService.getDetailPayment(paymentId);
    }

    @PostMapping
    public Payment createPayment(@RequestBody PaymentRequest paymentRequest) {
        return paymentService.createPayment(paymentRequest);
    }

    @PutMapping("/{paymentId}")
    public Payment updatePayment(@PathVariable Long paymentId, @RequestBody Payment payment) {
        return paymentService.updatePayment(paymentId, payment);
    }

    @DeleteMapping("/{paymentId}")
    public void deletePayment(@PathVariable Long paymentId) {
        paymentService.deletePayment(paymentId);
    }
}
