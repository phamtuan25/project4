package com.example.projectbackend.bean.response;

import com.example.projectbackend.entity.BookingDetail;
import com.example.projectbackend.entity.Room;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomResponse {
    private String roomNumber;
    private String roomType;
    private Room.RoomStatus status;
    private BigDecimal price;
    private LocalDate createdAt;
    private LocalDate updatedAt;
    private List<BookingDetail> bookingDetails;
}
