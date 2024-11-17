package com.example.projectbackend.entity;

import com.example.projectbackend.bean.request.BookingDetailRequest;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "bookings")
public class Booking {
    public enum BookingStatus {
        PENDING,
        COMPLETED,
        FAILED,
        PAID,
        DEPOSITED
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

    @OneToOne(mappedBy = "booking", cascade = CascadeType.ALL)
    private Payment payment;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        // Tính toán tổng tiền (totalAmount) khi booking được tạo
        calculateTotalAmount();
    }

    private double calculateRoomPrice(BookingDetail bookingDetail) {
        Room room = bookingDetail.getRoom();
        LocalDateTime checkIn = bookingDetail.getCheckIn();
        LocalDateTime checkOut = bookingDetail.getCheckOut();

        // Tính tổng số phút giữa checkIn và checkOut
        long minutes = Duration.between(checkIn, checkOut).toMinutes();

        // Tính số giờ, làm tròn lên nếu có phần dư
        long hours = (minutes + 59) / 60;  // Làm tròn lên theo giờ

        // Tính số ngày, làm tròn lên nếu có phần dư
        long days = Duration.between(checkIn, checkOut).toDays();
        days = Math.max(days, 1);  // Nếu số ngày < 1, tính luôn là 1 ngày

        double roomPrice = 0;

        // Nếu số giờ < 24 (thời gian ít hơn một ngày), tính giá theo giờ
        if (hours < 24) {
            roomPrice = room.getHourPrice() * hours;
        } else {
            // Nếu số ngày >= 1, tính giá theo ngày
            roomPrice = room.getDayPrice() * days;
        }

        return roomPrice;
    }


    public void calculateTotalAmount() {
        double totalAmount = 0;

        // Duyệt qua từng BookingDetail để tính tổng giá trị
        for (BookingDetail bookingDetail : bookingDetails) {
            double roomPrice = calculateRoomPrice(bookingDetail);

            // Gán giá phòng vào thuộc tính price của bookingDetail
            bookingDetail.setPrice(roomPrice); // Gắn giá phòng vào BookingDetail

            double provisionsPrice = 0;

            // Tính tổng giá trị của provisions liên quan đến bookingDetail
            if (bookingDetail.getRelProvisionBookingDetails() != null) {
                for (RelProvisionBookingDetail rel : bookingDetail.getRelProvisionBookingDetails()) {
                    // Chỉ cộng price nếu status không phải UNUSED
                    if (rel.getStatus() != RelProvisionBookingDetail.RelProvisionBookingDetailStatus.UNUSED) {
                        provisionsPrice += rel.getProvision().getPrice().doubleValue();
                    }
                }
            }

            // Cộng giá phòng và giá các provision vào tổng số tiền cho booking
            totalAmount += roomPrice + provisionsPrice;
        }

        // Gán tổng tiền vào thuộc tính totalAmount của Booking
        this.totalAmount = totalAmount;

        // Tính deposit (10% của tổng số tiền)
        this.deposit = totalAmount * 0.10;
    }



}

