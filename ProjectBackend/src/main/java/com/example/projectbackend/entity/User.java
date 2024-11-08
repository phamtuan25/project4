package com.example.projectbackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Entity
@Table(name = "users")
public class User {
    public enum RoleType {
        CUSTOMER,
        ADMIN,
        MANAGER,
        EMPLOYEE
    }
    public static List<String> getRolesAsList() {
        return Arrays.stream(RoleType.values())
                .map(Enum::name)
                .collect(Collectors.toList());
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

    @Column(name = "updated_at",updatable = false,columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Booking> bookings;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Contact> contacts;
    public void setFirstName(String firstName) {
        this.firstName = firstName;
        updateFullName();
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
        updateFullName();
    }

    private void updateFullName() {
        this.fullName = this.firstName + " " + this.lastName;
    }
}
