package com.example.projectbackend.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "Users")
public class User {
    public enum RoleType {
        CUSTOMER,
        ADMIN,
        MANAGER,
        EMPLOYEE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "firstname")
    private String firstName;

    @Column(name = "lastname")
    private String lastName;

    @Column(name = "fullname", updatable = false)
    private String fullName;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "phone")
    private String phoneNumber;

    @Column(name = "address")
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private RoleType role;

    @Column(name = "password")
    private String password;

    @Column(name = "updated_at",updatable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Booking> bookings;

}
