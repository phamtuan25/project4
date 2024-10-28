package com.example.projectbackend.service.impl;

import com.example.projectbackend.bean.request.PaymentRequest;
import com.example.projectbackend.bean.response.PaymentResponse;
import com.example.projectbackend.entity.Payment;
import com.example.projectbackend.exception.EmptyListException;
import com.example.projectbackend.exception.NotFoundException;
import com.example.projectbackend.mapper.PaymentMapper;
import com.example.projectbackend.repository.PaymentRepository;
import com.example.projectbackend.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    @Override
    public List<PaymentResponse> getAllPayments() {
        if(paymentRepository.findAll().isEmpty()) {
            throw new EmptyListException("Payment","This list Payment is empty");
        }
        return paymentRepository.findAll().stream().map(PaymentMapper::convertToResponse).collect(Collectors.toList());
    }

    @Override
    public PaymentResponse getDetailPayment(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId).orElse(null);
        if(Objects.isNull(payment)){
            throw new NotFoundException("PaymentNotFound","Not found Payment with " + paymentId);
        }
        return PaymentMapper.convertToResponse(paymentRepository.findById(paymentId).orElse(null));
    }

    @Override
    public Payment createPayment(PaymentRequest paymentRequest) {
        Payment payment = PaymentMapper.convertFromRequest(paymentRequest);
        return paymentRepository.save(payment);
    }

    @Override
    public Payment updatePayment(Long paymentId, Payment payment) {
        Payment paymentUpdate = paymentRepository.findById(paymentId).orElse(null);
        if(paymentUpdate!= null){
            setPayment(paymentUpdate, payment);
        }
        return paymentRepository.save(paymentUpdate);
    }

    @Override
    public void deletePayment(Long paymentId) {
        Optional<Payment> payment = paymentRepository.findById(paymentId);

        if (payment.isEmpty()) {
            throw new NotFoundException("PaymentNotFound","Payment with ID " + paymentId + " not found.");
        }
        paymentRepository.deleteById(paymentId);
    }

    public void setPayment(Payment paymentUpdate, Payment paymentInput){
        paymentUpdate.setPaymentMethod(paymentInput.getPaymentMethod());
        paymentUpdate.setStatus(paymentInput.getStatus());
    }


}
