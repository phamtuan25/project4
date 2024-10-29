package com.example.projectbackend.bean.request;

import com.example.projectbackend.entity.Image;
import com.example.projectbackend.entity.Room;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomRequest {
    private String roomNumber;
    private Room.RoomType roomType;
    private Room.RoomStatus status;
    private Double dayPrice;
    private Double hourPrice;
    private List<Image> images;
}
