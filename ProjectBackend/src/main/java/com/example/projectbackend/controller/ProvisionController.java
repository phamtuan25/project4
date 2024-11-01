package com.example.projectbackend.controller;


import com.example.projectbackend.bean.request.ImageRequest;
import com.example.projectbackend.bean.request.ProvisionRequest;
import com.example.projectbackend.bean.response.ProvisionResponse;
import com.example.projectbackend.entity.Provision;
import com.example.projectbackend.service.ImageService;
import com.example.projectbackend.service.ProvisionService;
import jakarta.validation.Valid;
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
@RequestMapping("/api/provisions")
public class ProvisionController {

    @Value("${upload.dir}")
    private String uploadDir;

    private final ProvisionService provisionService;
    private final ImageService imageService;

    @GetMapping
    public List<ProvisionResponse> getAllProvision() {
        return provisionService.getAllProvision();
    }

    @GetMapping("/{provisionId}")
    public ProvisionResponse getDetailServices(@PathVariable Long provisionId) {
        return provisionService.getDetailProvision(provisionId);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping(consumes = {MULTIPART_FORM_DATA_VALUE, APPLICATION_JSON_VALUE})
    public Provision createProvision(@Valid @RequestPart("provisionRequest") ProvisionRequest provisionRequest,
                           @RequestPart(value = "files", required = false) MultipartFile[] files) {
        try {
            // Tạo mới một Provision
            Provision provision = provisionService.createProvision(provisionRequest);

            // Lưu hình ảnh
            ImageRequest imageRequest = new ImageRequest();
            if (files != null && files.length > 0) {
                for (MultipartFile file : files) {
                    // Thiết lập thông tin hình ảnh
                    imageRequest.setReferenceId(provision.getProvisionId()); // Đặt ID tham chiếu
                    imageRequest.setName("PROVISION"); // Đặt Type ảnh
                    imageRequest.setImageFileName(file.getOriginalFilename()); // Lấy tên tệp từ MultipartFile

                    // Lưu hình ảnh với tên tương ứng
                    imageService.saveImages(file, imageRequest);
                }
            }
            return provision;
        } catch (IOException e) {
            throw new RuntimeException("Error while adding images: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("An unexpected error occurred: " + e.getMessage(), e);
        }
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PutMapping("/{provisionId}")
    public Provision updateProvision(@PathVariable Long provisionId, @RequestBody Provision provision) {
        return provisionService.updateProvision(provisionId, provision);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @DeleteMapping("/{provisionId}")
    public void deleteProvision(@PathVariable Long provisionId) {
        provisionService.deleteProvision(provisionId);
    }
}


