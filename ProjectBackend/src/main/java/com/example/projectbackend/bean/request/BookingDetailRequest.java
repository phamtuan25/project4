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
import java.util.List;

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

    private String specialRequests;

    private Double price;

    private List<Long> provisionIds;
}
