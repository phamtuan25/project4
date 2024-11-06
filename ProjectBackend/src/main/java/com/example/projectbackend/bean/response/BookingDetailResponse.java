package com.example.projectbackend.bean.response;

import com.example.projectbackend.entity.BookingDetail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingDetailResponse {
    private Long bookingDetailId;
    private Long bookingId;
    private Long roomId;
    private String roomNumber;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private BookingDetail.BookingDetailStatus status;
    private String specialRequests;
    private Double price;
}
