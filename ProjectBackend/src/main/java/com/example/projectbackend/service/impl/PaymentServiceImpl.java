package com.example.projectbackend.service.impl;

import com.example.projectbackend.bean.request.PaymentRequest;
import com.example.projectbackend.bean.response.BookingResponse;
import com.example.projectbackend.bean.response.PaymentResponse;
import com.example.projectbackend.entity.Booking;
import com.example.projectbackend.entity.Payment;
import com.example.projectbackend.entity.User;
import com.example.projectbackend.exception.EmptyListException;
import com.example.projectbackend.exception.NotFoundException;
import com.example.projectbackend.mapper.BookingMapper;
import com.example.projectbackend.mapper.PaymentMapper;
import com.example.projectbackend.repository.BookingRepository;
import com.example.projectbackend.repository.PaymentRepository;
import com.example.projectbackend.repository.UserRepository;
import com.example.projectbackend.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository; // Thêm repository để truy vấn thông tin Booking
    private final UserRepository userRepository;

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
        // Tìm thông tin booking từ bookingId
        Booking booking = bookingRepository.findById(paymentRequest.getBookingId())
                .orElseThrow(() -> new NotFoundException("BookingNotFound", "Not found Booking with ID " + paymentRequest.getBookingId()));

        // Xác định số tiền thanh toán: có thể là deposit hoặc totalAmount
        Double paidAmount = null;
        if (paymentRequest.getStatus() == Payment.PaymentStatus.PAID) {
            // Thanh toán toàn bộ số tiền
            paidAmount = booking.getTotalAmount();
        } else if (paymentRequest.getStatus() == Payment.PaymentStatus.DEPOSITED) {
            // Thanh toán tiền cọc
            paidAmount = booking.getDeposit();
        } else {
            throw new IllegalArgumentException("Invalid payment status for creating payment.");
        }

        // Tạo Payment mới
        Payment payment = new Payment();
        payment.setBooking(booking);
        payment.setStatus(paymentRequest.getStatus());
        payment.setPaid(paidAmount);

        // Tạo paymentReference (giả sử có một phương thức getFullName() để lấy tên người dùng)
        User user = booking.getUser();  // Lấy người dùng từ Booking
        String paymentReference = user.getFullName() + "PAID"; // Tạo paymentReference với full name + số tiền thanh toán
        payment.setPaymentReference(paymentReference);

        // Thiết lập ngày thanh toán
        payment.setPaymentDate(LocalDate.now());

        // Lưu Payment vào cơ sở dữ liệu
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
