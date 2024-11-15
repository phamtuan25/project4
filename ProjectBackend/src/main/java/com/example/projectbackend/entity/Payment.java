package com.example.projectbackend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "payments")
public class Payment {
    public enum PaymentMethod {
        CREDIT_CARD,
        PAYPAL,
        CASH,
        BANK_TRANSFER,
        MOBILE_PAYMENT
    }

    public enum PaymentStatus {
        ACTIVE,
        INACTIVE
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long paymentId;

    @OneToOne
    @JoinColumn(name = "booking_id", unique = true)
    private Booking booking;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private PaymentStatus status;

}
