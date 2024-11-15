package com.example.projectbackend.service.impl;

import com.example.projectbackend.bean.request.PaymentRequest;
import com.example.projectbackend.bean.response.BookingResponse;
import com.example.projectbackend.bean.response.PaymentResponse;
import com.example.projectbackend.entity.Booking;
import com.example.projectbackend.entity.Payment;
import com.example.projectbackend.exception.EmptyListException;
import com.example.projectbackend.exception.NotFoundException;
import com.example.projectbackend.mapper.BookingMapper;
import com.example.projectbackend.mapper.PaymentMapper;
import com.example.projectbackend.repository.PaymentRepository;
import com.example.projectbackend.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    @Override
    public Page<PaymentResponse> getAllPayments(@PageableDefault(size = 10, page = 0) Pageable pageable,
                                                @RequestParam(required = false) String keyword) {
        Specification<Payment> spec = searchByKeyword(keyword);
        Page<Payment> paymentPage = paymentRepository.findAll(spec, pageable);
        return paymentPage.map(payment -> {
            PaymentResponse paymentResponse = PaymentMapper.convertToResponse(payment);
            return paymentResponse;
        });
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

    public static Specification<Payment> searchByKeyword(String keyword) {
        return (root, query, criteriaBuilder) -> {
            if (keyword == null || keyword.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            String likePattern = "%" + keyword + "%";
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("status").as(String.class)), likePattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("paymentMethod").as(String.class)), likePattern)
            );
        };
    }
}
