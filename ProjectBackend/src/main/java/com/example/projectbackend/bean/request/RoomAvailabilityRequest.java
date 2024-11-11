package com.example.projectbackend.bean.request;

import com.example.projectbackend.entity.Room;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomAvailabilityRequest {
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private Room.RoomType roomType;

}



