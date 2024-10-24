package com.example.projectbackend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;



@Data
@Entity
@Table(name = "rel_provision_bookingdetails")
public class Rel_Provision_BookingDetail {
    public enum Rel_Provision_BookingDetailStatus {
        USED,
        UNUSED
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "provision_id")
    private Provision provision;

    @ManyToOne
    @JoinColumn(name = "booking_detail_id")
    private BookingDetail bookingDetail;

    @Column (name = "price")
    private Double price;

    @Column(name = "created_at",updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(name ="status")
    private Rel_Provision_BookingDetailStatus status;
}
