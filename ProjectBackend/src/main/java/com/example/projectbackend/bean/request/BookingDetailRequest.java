package com.example.projectbackend.bean.request;

import com.example.projectbackend.entity.BookingDetail;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingDetailRequest {
    @NotNull(message = "Booking ID is required")
    private Long bookingId;

    @NotNull(message = "Room ID is required")
    private Long roomId;

    @NotNull(message = "Check-in time is required")
    private LocalDateTime checkIn;

    @NotNull(message = "Check-out time is required")
    private LocalDateTime checkOut;

    @NotEmpty(message = "Special requests cannot be empty")
    private String specialRequests;

//    @NotNull(message = "Price is required")
//    @PositiveOrZero(message = "Price must be non-negative")
    private Double price;

    private BookingDetail.BookingDetailStatus status;
}
