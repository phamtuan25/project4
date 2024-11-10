package com.example.projectbackend.entity;

import com.example.projectbackend.bean.request.BookingDetailRequest;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "bookings")
public class Booking {
    public enum BookingStatus {
        PENDING,
        COMPLETED,
        FAILED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_id")
    private Long bookingId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "created_at", updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(name = "updated_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;

    @JsonIgnore
    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL)
    private List<BookingDetail> bookingDetails;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private BookingStatus status;

    @Column(name = "deposit")
    private Double deposit;

    @Column(name = "total_amount")
    private Double totalAmount;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        // Tính toán tổng tiền (totalAmount) khi booking được tạo
        calculateTotalAmount();
    }

    // Phương thức tính toán tổng tiền của booking từ các booking details và provisions
    public void calculateTotalAmount() {
        double totalAmount = 0;

        for (BookingDetail bookingDetail : bookingDetails) {
            // Tính giá của từng bookingDetail = giá phòng + các provision liên quan
            double roomPrice = bookingDetail.getRoom().getPrice();
            double provisionsPrice = 0;

            // Tính tổng giá trị của provisions liên quan đến bookingDetail
            if (bookingDetail.getRelProvisionBookingDetails() != null) {
                for (RelProvisionBookingDetail rel : bookingDetail.getRelProvisionBookingDetails()) {
                    provisionsPrice += rel.getProvision().getPrice().doubleValue();
                }
            }

            // Cộng giá phòng và giá các provision vào tổng số tiền cho booking
            totalAmount += roomPrice + provisionsPrice + deposit;
        }

        // Gán tổng tiền vào thuộc tính totalAmount của Booking
        this.totalAmount = totalAmount;
    }
}

