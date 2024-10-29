package com.example.projectbackend.bean.request;


import com.example.projectbackend.entity.Booking;
import com.example.projectbackend.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequest {
    private User user;
    private Booking.BookingStatus status;
    private Double deposit;
    private Double totalAmount;
}
