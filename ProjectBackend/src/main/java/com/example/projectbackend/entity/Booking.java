package com.example.projectbackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

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
        calculateTotalAmount();
    }

    private double calculateRoomPrice(BookingDetail bookingDetail) {
        Room room = bookingDetail.getRoom();
        LocalDateTime checkIn = bookingDetail.getCheckIn();
        LocalDateTime checkOut = bookingDetail.getCheckOut();

        long minutes = Duration.between(checkIn, checkOut).toMinutes();

        long hours = (minutes + 59) / 60;

        long days = Duration.between(checkIn, checkOut).toDays();
        days = Math.max(days, 1);

        double roomPrice = 0;

        if (hours < 24) {
            roomPrice = room.getHourPrice() * hours;
        } else {
            roomPrice = room.getDayPrice() * days;
        }

        return roomPrice;
    }


    public void calculateTotalAmount() {
        double totalAmount = 0;

        for (BookingDetail bookingDetail : bookingDetails) {
            double roomPrice = calculateRoomPrice(bookingDetail);

            bookingDetail.setPrice(roomPrice);

            double provisionsPrice = 0;

            if (bookingDetail.getRelProvisionBookingDetails() != null) {
                for (RelProvisionBookingDetail rel : bookingDetail.getRelProvisionBookingDetails()) {
                    if (rel.getStatus() != RelProvisionBookingDetail.RelProvisionBookingDetailStatus.UNUSED) {
                        provisionsPrice += rel.getProvision().getPrice().doubleValue();
                    }
                }
            }

            totalAmount += roomPrice + provisionsPrice;
        }

        this.totalAmount = totalAmount;

        this.deposit = totalAmount * 0.10;
    }

}

