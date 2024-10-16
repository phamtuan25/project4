package com.example.projectbackend.mapper;

import com.example.projectbackend.bean.request.RoomRequest;
import com.example.projectbackend.bean.response.RoomResponse;
import com.example.projectbackend.entity.Room;

public class RoomMapper {
    public static RoomResponse convertToResponse(Room room){
        RoomResponse roomResponse = new RoomResponse();
        roomResponse.setRoomNumber(room.getRoomNumber());
        roomResponse.setRoomType(room.getRoomType());
        roomResponse.setStatus(room.getStatus());
        roomResponse.setPrice(room.getPrice());
        roomResponse.setCreatedAt(room.getCreatedAt());
        roomResponse.setUpdatedAt(room.getUpdatedAt());
        roomResponse.setBookingDetails(room.getBookingDetails());
        return roomResponse;
    }

    public static Room convertFromRequest(RoomRequest roomRequest){
        Room room = new Room();
        room.setRoomNumber(roomRequest.getRoomNumber());
        room.setRoomType(roomRequest.getRoomType());
        room.setStatus(roomRequest.getStatus());
        room.setPrice(roomRequest.getPrice());
        return room;
    }
}
