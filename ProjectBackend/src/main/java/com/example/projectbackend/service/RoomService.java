package com.example.projectbackend.service;

import com.example.projectbackend.bean.request.RoomRequest;
import com.example.projectbackend.bean.response.RoomResponse;
import com.example.projectbackend.entity.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RoomService {
    public Page<RoomResponse> getAllRooms(Pageable pageable, String keyword, String status);
    public RoomResponse getDetailRoom(Long roomId);
    public Room createRoom(RoomRequest roomRequest);
    public Room updateRoom(Long roomId, RoomRequest roomRequest);
    public void deleteRoom(Long roomId);
}
