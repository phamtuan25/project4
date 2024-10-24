package com.example.projectbackend.controller;

import com.example.projectbackend.bean.request.RoomRequest;
import com.example.projectbackend.bean.response.RoomResponse;
import com.example.projectbackend.entity.Room;
import com.example.projectbackend.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rooms")
public class RoomController {
    private final RoomService roomService;
    @GetMapping
    public List<RoomResponse> getAllRooms() {
        return roomService.getAllRooms();
    }

    @GetMapping("/{roomId}")
    public RoomResponse getDetailRoom(@PathVariable Long roomId) {
        return roomService.getDetailRoom(roomId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public Room createRoom(@RequestBody RoomRequest roomRequest) {
        return roomService.createRoom(roomRequest);
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'EMPLOYEE')")
    @PutMapping("/{roomId}")
    public Room updateRoom(@PathVariable Long roomId, @RequestBody Room room) {
        return roomService.updateRoom(roomId, room);
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'EMPLOYEE')")
    @DeleteMapping("/{roomId}")
    public void deleteRoom(@PathVariable Long roomId) {
        roomService.deleteRoom(roomId);
    }
}
