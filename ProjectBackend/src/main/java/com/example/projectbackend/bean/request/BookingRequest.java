package com.example.projectbackend.bean.request;

import com.example.projectbackend.entity.Booking;
import com.example.projectbackend.entity.BookingDetail;
import com.example.projectbackend.entity.User;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequest {
    @NotEmpty(message = "Room numbers are required")
    private List<BookingDetailRequest> bookingDetailRequests;

    @NotNull(message = "User is required")
    private User user;

    @NotNull(message = "Booking status is required")
    private Booking.BookingStatus status;

    @NotNull(message = "Deposit is required")
    private Double deposit;

    @NotNull(message = "Total amount is required")
    private Double totalAmount;

}
