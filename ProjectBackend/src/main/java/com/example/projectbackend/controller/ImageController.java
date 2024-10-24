package com.example.projectbackend.controller;

import com.example.projectbackend.bean.request.ImageRequest;
import com.example.projectbackend.bean.response.BookingResponse;
import com.example.projectbackend.bean.response.ImageResponse;
import com.example.projectbackend.entity.Image;
import com.example.projectbackend.service.ImageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/images")
public class ImageController {
    private final String uploadDir = "./../../";

    private final ImageService imageService;

    @GetMapping("/{name}/{refId}")
    public List<Image> getImages(@PathVariable("refId") Long refId, @PathVariable("name") String name) {
        return imageService.getImages(name, refId);
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping(consumes = {MULTIPART_FORM_DATA_VALUE, APPLICATION_JSON_VALUE})
    public List<ImageResponse> addImages(
            @RequestParam("files") MultipartFile[] files,
            @RequestPart("imageDetails") @Valid List<ImageRequest> imageRequests) throws IOException {

        if (files.length != imageRequests.size()) {
            throw new IllegalArgumentException("The number of image files must match the number of image details.");
        }
        List<ImageResponse> savedImages = new ArrayList<>();
        for (int i = 0; i < imageRequests.size(); i++) {
            ImageRequest imageRequest = imageRequests.get(i);
            MultipartFile file = files[i];
            if (file != null && !file.isEmpty()) {
                String fileName = generateUniqueFileName(file.getOriginalFilename());
                Files.copy(file.getInputStream(), Paths.get(uploadDir).resolve(fileName));
                imageRequest.setImage(fileName);
            }
            ImageResponse savedImage = imageService.saveImage(imageRequest);
            savedImages.add(savedImage);
        }
        return savedImages;
    }
    private String generateUniqueFileName(String originalFilename) {
        String datePart = LocalDate.now().toString();
        String uniqueId = UUID.randomUUID().toString();
        return uniqueId + "_" + datePart + "_" + originalFilename;
    }

}
