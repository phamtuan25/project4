package com.example.projectbackend.bean.request;

import com.example.projectbackend.entity.Image;
import com.example.projectbackend.entity.Room;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomRequest {

    @NotEmpty(message = "Room number is required")
    private String roomNumber;

    @NotNull(message = "Room type is required")
    private Room.RoomType roomType;

    @NotNull(message = "Room status is required")
    private Room.RoomStatus status;

    @NotNull(message = "Day price is required")
    @Positive(message = "Day price must be positive")
    private Double dayPrice;

    @NotNull(message = "Hour price is required")
    @Positive(message = "Hour price must be positive")
    private Double hourPrice;

    private List<ImageRequest> images;
}
