package com.example.projectbackend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "rel_provision_bookingdetails")
public class RelProvisionBookingDetail {
    public enum RelProvisionBookingDetailStatus {
        USED,
        UNUSED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rel_id")
    private Long relId;

    @ManyToOne
    @JoinColumn(name = "provision_id")
    private Provision provision;

    @ManyToOne
    @JoinColumn(name = "booking_detail_id")
    private BookingDetail bookingDetail;

    @Column(name = "booking_id")
    private Long bookingId;

    @Column(name = "price")
    private Double price;

    @Column(name = "created_at", updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private RelProvisionBookingDetailStatus status;

    @PrePersist
    @PreUpdate
    private void updatePrice() {
        if (provision != null) {
            this.price = provision.getPrice().doubleValue();
        }
    }
}
