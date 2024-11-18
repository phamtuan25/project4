package com.example.projectbackend.service.impl;

import com.example.projectbackend.bean.request.RoomRequest;
import com.example.projectbackend.bean.response.ImageResponse;
import com.example.projectbackend.bean.response.RoomResponse;
import com.example.projectbackend.entity.BookingDetail;
import com.example.projectbackend.entity.Room;
import com.example.projectbackend.exception.NotFoundException;
import com.example.projectbackend.mapper.ImageMapper;
import com.example.projectbackend.mapper.RoomMapper;
import com.example.projectbackend.repository.BookingDetailRepository;
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

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;
    private final ImageRepository imageRepository;
    private final BookingDetailRepository bookingDetailRepository;

    @Override
    public Page<RoomResponse> getAllRooms(@PageableDefault(size = 10, page = 0) Pageable pageable,
                                          @RequestParam(required = false) String keyword,
                                          @RequestParam(required = false) String status,
                                          @RequestParam(required = false) String roomType) {

        Specification<Room> spec = searchByKeywordStatusAndRoomType(keyword, status, roomType);

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
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new RuntimeException("The room does not exist."));

        if (roomRequest.getStatus() != null) {
            room.setStatus(roomRequest.getStatus());
        }
        if (roomRequest.getDescription() != null) {
            room.setDescription(roomRequest.getDescription());
        }
        if (roomRequest.getDayPrice() != null) {
            room.setDayPrice(roomRequest.getDayPrice());
        }
        if (roomRequest.getHourPrice() != null) {
            room.setHourPrice(roomRequest.getHourPrice());
        }


        return roomRepository.save(room);
    }



    @Override
    public void deleteRoom(Long roomId) {
        Optional<Room> room = roomRepository.findById(roomId);

        if (room.isEmpty()) {
            throw new NotFoundException("RoomNotFound", "Room with ID " + roomId + " not found.");
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

    public static Specification<Room> searchByKeywordStatusAndRoomType(String keyword, String status, String roomType) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (keyword != null && !keyword.isEmpty()) {
                String likePattern = "%" + keyword.toLowerCase() + "%";
                predicates.add(criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("roomNumber")), likePattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("roomType")), likePattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("status")), likePattern)
                ));
            }

            if (status != null && !status.isEmpty()) {
                predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("status")), status.toLowerCase()));
            }
            if (roomType != null) {
                predicates.add(criteriaBuilder.equal(root.get("roomType"), roomType));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }



    @Override
    public List<RoomResponse> findAvailableRooms(LocalDateTime checkIn, LocalDateTime checkOut, Room.RoomType roomType) {
        List<Room> rooms = roomRepository.findByRoomType(roomType);

        List<RoomResponse> availableRooms = new ArrayList<>();
        for (Room room : rooms) {
            if (room.getStatus() == Room.RoomStatus.MAINTENANCE) {
                continue;
            }

            if (room.getStatus() == Room.RoomStatus.AVAILABLE) {
                RoomResponse roomResponse = RoomMapper.convertToResponse(room);
                availableRooms.add(roomResponse);
                continue;
            }

            if (room.getStatus() == Room.RoomStatus.BOOKED) {
                List<BookingDetail> bookingDetails = bookingDetailRepository.findByRoom(room);
                bookingDetails.sort((bd1, bd2) -> bd2.getBookingDetailId().compareTo(bd1.getBookingDetailId()));

                for (BookingDetail bookingDetail : bookingDetails) {
                    if (bookingDetail.getStatus() != BookingDetail.BookingDetailStatus.CANCELED) {
                        if (checkIn.isAfter(bookingDetail.getCheckOut()) && bookingDetail.getCheckOut().isBefore(LocalDateTime.now())) {
                            room.setStatus(Room.RoomStatus.AVAILABLE);
                            roomRepository.save(room);

                            RoomResponse roomResponse = RoomMapper.convertToResponse(room);
                            availableRooms.add(roomResponse);
                        } else if (checkIn.isAfter(bookingDetail.getCheckOut()) && bookingDetail.getCheckOut().isAfter(LocalDateTime.now())) {
                            RoomResponse roomResponse = RoomMapper.convertToResponse(room);
                            availableRooms.add(roomResponse);
                        }
                        break;
                    }
                }
            }
        }

        for (RoomResponse roomResponse : availableRooms) {
            List<String> images = imageRepository.findAllByNameAndReferenceId("ROOM", roomResponse.getRoomId())
                    .stream()
                    .map(ImageMapper::convertToResponse)
                    .map(ImageResponse::getImageFileName)
                    .collect(Collectors.toList());

            roomResponse.setImages(images);
        }

        return availableRooms;
    }

    public boolean isRoomAvailable(Room room, LocalDateTime checkIn, LocalDateTime checkOut) {
        List<BookingDetail> bookingDetails = bookingDetailRepository.findByRoom(room);

        for (BookingDetail bookingDetail : bookingDetails) {
            if (bookingDetail.getStatus() != BookingDetail.BookingDetailStatus.CANCELED) {
                LocalDateTime bookingCheckIn = bookingDetail.getCheckIn();
                LocalDateTime bookingCheckOut = bookingDetail.getCheckOut();

                if ((checkIn.isBefore(bookingCheckOut) && checkOut.isAfter(bookingCheckIn))) {
                    return false;
                }
            }
        }

        return true;
    }

    public void updateRoomStatusIfNecessary(Room room, LocalDateTime checkIn) {
        List<BookingDetail> bookingDetails = bookingDetailRepository.findByRoom(room);
        for (BookingDetail bookingDetail : bookingDetails) {
            if (bookingDetail.getStatus() != BookingDetail.BookingDetailStatus.CANCELED) {
                if (bookingDetail.getCheckOut().isBefore(checkIn)) {
                    room.setStatus(Room.RoomStatus.AVAILABLE);
                    roomRepository.save(room);
                    break;
                }
            }
        }
    }





}
