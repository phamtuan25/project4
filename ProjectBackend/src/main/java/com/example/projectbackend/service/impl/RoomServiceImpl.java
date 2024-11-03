package com.example.projectbackend.service.impl;

import com.example.projectbackend.bean.request.RoomRequest;
import com.example.projectbackend.bean.response.ImageResponse;
import com.example.projectbackend.bean.response.ProvisionResponse;
import com.example.projectbackend.bean.response.RoomResponse;
import com.example.projectbackend.entity.Provision;
import com.example.projectbackend.entity.Room;
import com.example.projectbackend.exception.EmptyListException;
import com.example.projectbackend.exception.NotFoundException;
import com.example.projectbackend.mapper.ImageMapper;
import com.example.projectbackend.mapper.ProvisionMapper;
import com.example.projectbackend.mapper.RoomMapper;
import com.example.projectbackend.repository.ImageRepository;
import com.example.projectbackend.repository.RoomRepository;
import com.example.projectbackend.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;
    private final ImageRepository imageRepository;

    public Page<RoomResponse> getAllRooms(@PageableDefault(size = 10, page = 0)Pageable pageable,
                                            @RequestParam(required = false) String keyword) {
        Specification<Room> spec = searchByKeyword(keyword);
        Page<Room> roomsPage = roomRepository.findAll(spec, pageable);

        return roomsPage.map(room -> {
            RoomResponse roomResponse = RoomMapper.convertToResponse(room);

            // Lấy danh sách ảnh và chuyển đổi chúng sang ImageResponse
            List<String> images = imageRepository.findAllByNameAndReferenceId("ROOM", room.getRoomId())
                    .stream()
                    .map(ImageMapper::convertToResponse)
                    .map(ImageResponse::getImageFileName)
                    .collect(Collectors.toList());

            roomResponse.setImages(images);
            return roomResponse;
        });
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
        return roomRepository.save(room);
    }

    @Override
    public Room updateRoom(Long roomId, RoomRequest roomRequest) {
        Room roomUpdate = roomRepository.findById(roomId).orElse(null);
        if(roomUpdate != null){
            setRoom(roomUpdate, roomRequest);
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

    public void setRoom(Room roomUpdate, RoomRequest roomInput) {
        roomUpdate.setRoomNumber(roomInput.getRoomNumber());
        roomUpdate.setRoomType(roomInput.getRoomType());
        roomUpdate.setStatus(roomInput.getStatus());
        roomUpdate.setHourPrice(roomInput.getHourPrice());
        roomUpdate.setDayPrice(roomInput.getDayPrice());
    }

    public static Specification<Room> searchByKeyword(String keyword) {
        return (root, query, criteriaBuilder) -> {
            if (keyword == null || keyword.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            String likePattern = "%" + keyword + "%";
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("roomNumber").as(String.class)), likePattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("roomType").as(String.class)), likePattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("status").as(String.class)), likePattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("dayPrice").as(String.class)), likePattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("hourPrice").as(String.class)), likePattern)
            );
        };
    }
}
