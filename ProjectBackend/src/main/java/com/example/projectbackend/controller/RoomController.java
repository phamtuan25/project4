package com.example.projectbackend.controller;

import com.example.projectbackend.bean.request.ImageRequest;
import com.example.projectbackend.bean.request.ProvisionRequest;
import com.example.projectbackend.bean.request.RoomRequest;
import com.example.projectbackend.bean.response.RoomResponse;
import com.example.projectbackend.entity.Provision;
import com.example.projectbackend.entity.Room;
import com.example.projectbackend.service.ImageService;
import com.example.projectbackend.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    public List<RoomResponse> getAllRooms() {
        return roomService.getAllRooms();
    }

    @GetMapping("/{roomId}")
    public RoomResponse getDetailRoom(@PathVariable Long roomId) {
        return roomService.getDetailRoom(roomId);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping(consumes = {MULTIPART_FORM_DATA_VALUE, APPLICATION_JSON_VALUE})
    public Room createRoom(@RequestPart("roomRequest") RoomRequest roomRequest,
                           @RequestPart(value = "files", required = false) MultipartFile[] files) {
        try {
            // Tạo mới một Provision
            Room room = roomService.createRoom(roomRequest);

            // Lưu hình ảnh
            if (roomRequest.getImages() != null && !roomRequest.getImages().isEmpty()) {
                for (ImageRequest imageRequest : roomRequest.getImages()) {
                    imageRequest.setReferenceId(room.getRoomId()); // Đặt ID tham chiếu
                    // Lưu từng hình ảnh
                    for (MultipartFile file : files) {
                        imageService.saveImages(file, imageRequest);
                    }
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
    public Room updateRoom(@PathVariable Long roomId, @RequestBody Room room) {
        return roomService.updateRoom(roomId, room);
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'EMPLOYEE')")
    @DeleteMapping("/{roomId}")
    public void deleteRoom(@PathVariable Long roomId) {
        roomService.deleteRoom(roomId);
    }
}
