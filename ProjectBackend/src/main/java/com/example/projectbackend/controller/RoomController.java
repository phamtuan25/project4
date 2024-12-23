package com.example.projectbackend.controller;

import com.example.projectbackend.bean.request.ImageRequest;
import com.example.projectbackend.bean.request.RoomAvailabilityRequest;
import com.example.projectbackend.bean.request.RoomRequest;
import com.example.projectbackend.bean.response.RoomResponse;
import com.example.projectbackend.entity.Room;
import com.example.projectbackend.service.ImageService;
import com.example.projectbackend.service.RoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rooms")
public class RoomController {

    @Value("${upload.dir}")
    private String uploadDir;

    private final RoomService roomService;
    private final ImageService imageService;
    @GetMapping
    public Page<RoomResponse> getAllRooms(Pageable pageable, @RequestParam(required = false) String keyword, String status, String roomType) {
        return roomService.getAllRooms(pageable, keyword, status, roomType);
    }

    @GetMapping("/{roomId}")
    public RoomResponse getDetailRoom(@PathVariable Long roomId) {
        return roomService.getDetailRoom(roomId);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping(consumes = {MULTIPART_FORM_DATA_VALUE})
    public Room createRoom(@Valid @RequestPart("roomRequest") RoomRequest roomRequest,
                           @RequestPart(value = "files", required = false) MultipartFile[] files) {
        try {
            Room room = roomService.createRoom(roomRequest);

            ImageRequest imageRequest = new ImageRequest();
            if (files != null && files.length > 0) {
                for (MultipartFile file : files) {
                    imageRequest.setReferenceId(room.getRoomId());
                    imageRequest.setName("ROOM");
                    imageRequest.setImageFileName(file.getOriginalFilename());

                    imageService.saveImages(file, imageRequest);
                }
            }

            return room;
        } catch (IOException e) {
            throw new RuntimeException("Error while adding images: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("An unexpected error occurred: " + e.getMessage(), e);
        }
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'EMPLOYEE')")
    @PutMapping("/{roomId}")
    public Room updateRoom(@PathVariable Long roomId,
                           @RequestPart("roomRequest") RoomRequest roomRequest,
                           @RequestPart(value = "files", required = false) MultipartFile[] files) {
        try {
            Room updatedRoom = roomService.updateRoom(roomId, roomRequest);

            if (files != null && files.length > 0) {
                for (MultipartFile file : files) {
                    ImageRequest imageRequest = new ImageRequest();
                    imageRequest.setReferenceId(updatedRoom.getRoomId());
                    imageRequest.setName("ROOM");
                    imageRequest.setImageFileName(file.getOriginalFilename());
                    imageService.saveImages(file, imageRequest);
                }
            }

            if (roomRequest.getDeleteFiles() != null && roomRequest.getDeleteFiles().length > 0) {
                imageService.deleteImageByFileName(roomRequest.getDeleteFiles());
            }

            return updatedRoom;
        } catch (IOException e) {
            throw new RuntimeException("Lỗi khi cập nhật hình ảnh: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi không mong đợi: " + e.getMessage(), e);
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'EMPLOYEE')")
    @DeleteMapping("/{roomId}")
    public void deleteRoom(@PathVariable Long roomId) {
        roomService.deleteRoom(roomId);
    }


    @PostMapping("/available")
    public ResponseEntity<List<RoomResponse>> findAvailableRooms(@RequestBody RoomAvailabilityRequest request) {
        LocalDateTime checkIn = request.getCheckIn();
        LocalDateTime checkOut = request.getCheckOut();
        Room.RoomType roomType = request.getRoomType();

        List<RoomResponse> availableRooms = roomService.findAvailableRooms(checkIn, checkOut, roomType);

        if (availableRooms.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(availableRooms);
    }


}
