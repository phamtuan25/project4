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
    public enum PaymentStatus {
        PAID,
        DEPOSITED
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long paymentId;

    @OneToOne
    @JoinColumn(name = "booking_id", unique = true)
    private Booking booking;

    @Column(name = "paid_amount")
    private Double paid;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private PaymentStatus status;

}
