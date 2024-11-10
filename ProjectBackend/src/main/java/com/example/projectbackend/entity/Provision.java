package com.example.projectbackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.lang.annotation.Annotation;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "provisions")
public class Provision  {
    public enum ProvisionStatus {
        ACTIVE,
        INACTIVE
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "provision_id")
    private Long provisionId;

    @Column(name = "provision_name")
    private String provisionName;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private Double price;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ProvisionStatus status;

    @JsonIgnore
    @OneToMany(mappedBy = "provision")
    private List<RelProvisionBookingDetail> relProvisionBookingDetails;
}
