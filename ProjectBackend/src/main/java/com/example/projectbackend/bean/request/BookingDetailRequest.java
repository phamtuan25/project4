package com.example.projectbackend.bean.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingDetailRequest {
    private Long bookingId;
    private Long roomId;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private String specialRequests;
    private Double price;
}
