package com.example.projectbackend.bean.request;

import com.example.projectbackend.entity.Room;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomRequest {
    private String roomNumber;
    private String roomType;
    private Room.RoomStatus status;
    private BigDecimal price;
}
