package com.example.projectbackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "booking_details")
public class BookingDetail {
    public enum BookingDetailStatus {
        PENDING,
        CONFIRMED,
        CANCELED,
        COMPLETED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_detail_id")
    private Long bookingDetailId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "booking_id")
    private Booking booking;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "room_id")
    private Room room;

    @JsonIgnore
    @OneToMany(mappedBy = "bookingDetail", cascade = CascadeType.ALL)
    private List<RelProvisionBookingDetail> relProvisionBookingDetails = new ArrayList<>();

    @Column(name = "check_in")
    private LocalDateTime checkIn;

    @Column(name = "check_out")
    private LocalDateTime checkOut;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private BookingDetailStatus status;

    @Column(name = "created_at", updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(name = "updated_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;

    @Column(name = "special_requests", length = 1000)
    private String specialRequests;

    @Column(name = "price")
    private Double price;


    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}
