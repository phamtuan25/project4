package com.example.projectbackend.bean.response;

import com.example.projectbackend.entity.BookingDetail;
import com.example.projectbackend.entity.Image;
import com.example.projectbackend.entity.Room;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomResponse {
    private String roomNumber;
    private Room.RoomType roomType;
    private Room.RoomStatus status;
    private BigDecimal dayPrice;
    private BigDecimal hourPrice;
    private List<Image> images;
}