package com.example.projectbackend.service;

import com.example.projectbackend.bean.request.RoomRequest;
import com.example.projectbackend.bean.response.RoomResponse;
import com.example.projectbackend.entity.Room;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RoomService {
    public List<RoomResponse> getAllRooms();
    public RoomResponse getDetailRoom(Long roomId);
    public Room createRoom(RoomRequest roomRequest);
    public Room updateRoom(Long roomId, Room room);
    public void deleteRoom(Long roomId);
}
