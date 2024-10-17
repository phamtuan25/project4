package com.example.projectbackend.service.impl;

import com.example.projectbackend.bean.request.RoomRequest;
import com.example.projectbackend.bean.response.RoomResponse;
import com.example.projectbackend.entity.Room;
import com.example.projectbackend.exception.EmptyListException;
import com.example.projectbackend.exception.NotFoundException;
import com.example.projectbackend.mapper.RoomMapper;
import com.example.projectbackend.repository.RoomRepository;
import com.example.projectbackend.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;
    @Override
    public List<RoomResponse> getAllRooms() {
        if(roomRepository.findAll().isEmpty()) {
            throw new EmptyListException("EmptyRoom","This list Room is empty");
        }
        return roomRepository.findAll().stream().map(RoomMapper::convertToResponse).collect(Collectors.toList());
    }

    @Override
    public RoomResponse getDetailRoom(Long roomId) {
        Room room = roomRepository.findById(roomId).orElse(null);
        if(Objects.isNull(room)){
            throw new NotFoundException("RoomNotFound","Not found Room with " + roomId);
        }
        return RoomMapper.convertToResponse(roomRepository.findById(roomId).orElse(null));
    }

    @Override
    public Room createRoom(RoomRequest roomRequest) {
        Room room = RoomMapper.convertFromRequest(roomRequest);
        room.setCreatedAt(LocalDateTime.now());
        return roomRepository.save(room);
    }

    @Override
    public Room updateRoom(Long roomId, Room room) {
        Room roomUpdate = roomRepository.findById(roomId).orElse(null);
        if(roomUpdate != null){
            setRoom(roomUpdate, room);
        }
        return roomRepository.save(roomUpdate);
    }

    @Override
    public void deleteRoom(Long roomId) {
        Optional<Room> room = roomRepository.findById(roomId);

        if (room.isEmpty()) {
            throw new NotFoundException("RoomNotFound","Room with ID " + roomId + " not found.");
        }
        roomRepository.deleteById(roomId);
    }

    public void setRoom(Room roomUpdate, Room roomInput) {
        roomUpdate.setRoomNumber(roomInput.getRoomNumber());
        roomUpdate.setRoomType(roomInput.getRoomType());
        roomUpdate.setStatus(roomInput.getStatus());
        roomUpdate.setPrice(roomInput.getPrice());
        roomUpdate.setUpdatedAt(LocalDateTime.now());
    }
}
