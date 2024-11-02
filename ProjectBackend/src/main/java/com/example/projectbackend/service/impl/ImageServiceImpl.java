package com.example.projectbackend.service.impl;

import com.example.projectbackend.bean.request.ImageRequest;
import com.example.projectbackend.entity.Image;
import com.example.projectbackend.mapper.ImageMapper;
import com.example.projectbackend.repository.ImageRepository;
import com.example.projectbackend.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    private final ImageRepository imageRepository;

    @Value("${upload.dir}")
    public String uploadDir;

    @Override
    public String saveImages(MultipartFile file, ImageRequest imageRequest) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String newFilename = UUID.randomUUID().toString() + "_" + originalFilename;
        Path path = Paths.get(uploadDir + newFilename);

        // Log đường dẫn để kiểm tra
        System.out.println("Saving image to: " + path.toString());


        // Đảm bảo thư mục tồn tại
        Files.createDirectories(path.getParent());
        Files.write(path, file.getBytes());

        // Chuyển đổi từ ImageRequest sang Image
        Image image = ImageMapper.convertFromRequest(imageRequest);
        image.setImageFileName(newFilename); // Đặt tên tệp mới

        // Nếu bạn muốn lưu đường dẫn, hãy tạo một trường tương ứng trong thực thể Image
        image.setImagePath(path.toString()); // Giả sử bạn có trường imagePath trong entity
        image.setUploadDate(LocalDate.now());
        // Lưu hình ảnh vào cơ sở dữ liệu
        imageRepository.save(image);

        return newFilename;
    }

    @Override
    public String deleteImageByFileName(String[] fileNames) throws IOException {
        for (String fileName : fileNames) {
            // Tìm kiếm hình ảnh trong cơ sở dữ liệu bằng tên tệp
            Optional<Image> optionalImage = imageRepository.findByImageFileName(fileName);

            if (optionalImage.isPresent()) {
                Image image = optionalImage.get();

                // Xóa tệp khỏi hệ thống file
                Path path = Paths.get(uploadDir + fileName);
                Files.deleteIfExists(path);

                // Xóa hình ảnh khỏi cơ sở dữ liệu
                imageRepository.delete(image);
            } else {
                System.out.println("Image not found: " + fileName);
            }
        }
        return "Images deleted successfully.";
    }

}
