package com.example.projectbackend.bean.response;

import com.example.projectbackend.entity.Booking;
import com.example.projectbackend.entity.BookingDetail;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponse {
    private Long bookingId;
    private UserBookingResponse userBookingResponse;
    private List<BookingDetailResponse> bookingDetailResponses;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDateTime updatedAt;
    private Booking.BookingStatus status;
    private Double deposit;
    private Double totalAmount;
}
