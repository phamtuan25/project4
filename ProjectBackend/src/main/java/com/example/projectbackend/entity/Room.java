package com.example.projectbackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "rooms")
public class Room {
    public enum RoomStatus {
        AVAILABLE,
        BOOKED,
        MAINTENANCE
    }
    public enum RoomType {
        SINGLE,
        DOUBLE,
        DELUXE,
        FAMILY
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long roomId;

    @NotBlank(message = "Room name is required")
    @Column(name = "room_number", unique = true)
    private String roomNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "room_type")
    private RoomType roomType;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    @Column(name = "description", length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private RoomStatus status;

    @Column(name = "day_price")
    private Double dayPrice;

    @Column(name = "hour_price")
    private Double hourPrice;

    @JsonIgnore
    @OneToMany(mappedBy = "room")
    private List<BookingDetail> bookingDetails;

}
