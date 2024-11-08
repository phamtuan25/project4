package com.example.projectbackend.service.impl;

import com.example.projectbackend.bean.request.RoomRequest;
import com.example.projectbackend.bean.response.ImageResponse;
import com.example.projectbackend.bean.response.RoomResponse;
import com.example.projectbackend.entity.Room;
import com.example.projectbackend.exception.NotFoundException;
import com.example.projectbackend.mapper.ImageMapper;
import com.example.projectbackend.mapper.RoomMapper;
import com.example.projectbackend.repository.ImageRepository;
import com.example.projectbackend.repository.RoomRepository;
import com.example.projectbackend.service.RoomService;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.jpa.domain.Specification;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;
    private final ImageRepository imageRepository;
    @Override
    public Page<RoomResponse> getAllRooms(@PageableDefault(size = 10, page = 0)Pageable pageable,
                                            @RequestParam(required = false) String keyword,
                                          @RequestParam(required = false) String status){
        Specification<Room> spec = searchByKeywordAndStatus(keyword, status);
        Page<Room> roomsPage = roomRepository.findAll(spec, pageable);

        return roomsPage.map(room -> {
            RoomResponse roomResponse = RoomMapper.convertToResponse(room);

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
        if (Objects.isNull(room)) {
            throw new NotFoundException("RoomNotFound", "Not found Room with " + roomId);
        }
        RoomResponse roomResponse = RoomMapper.convertToResponse(room);
        List<String> images = imageRepository.findAllByNameAndReferenceId("ROOM", room.getRoomId())
                .stream()
                .map(ImageMapper::convertToResponse)
                .map(ImageResponse::getImageFileName)
                .collect(Collectors.toList());
        roomResponse.setImages(images);
        return roomResponse;
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

    public static Specification<Room> searchByKeywordAndStatus(String keyword, String status) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (keyword != null && !keyword.isEmpty()) {
                String likePattern = "%" + keyword.toLowerCase() + "%";
                predicates.add(
                        criteriaBuilder.or(
                                criteriaBuilder.like(criteriaBuilder.lower(root.get("roomNumber")), likePattern),
                                criteriaBuilder.like(criteriaBuilder.lower(root.get("roomType")), likePattern),
                                criteriaBuilder.like(criteriaBuilder.lower(root.get("status")), likePattern),
                                criteriaBuilder.like(criteriaBuilder.lower(root.get("dayPrice").as(String.class)), likePattern),
                                criteriaBuilder.like(criteriaBuilder.lower(root.get("hourPrice").as(String.class)), likePattern)
                        )
                );
            }
            if (status != null && !status.isEmpty()) {
                predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("status")), status.toLowerCase()));
            }
            if (predicates.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
