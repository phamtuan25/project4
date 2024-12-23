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

        System.out.println("Saving image to: " + path.toString());


        Files.createDirectories(path.getParent());
        Files.write(path, file.getBytes());

        Image image = ImageMapper.convertFromRequest(imageRequest);
        image.setImageFileName(newFilename);

        image.setImagePath(path.toString());
        image.setUploadDate(LocalDate.now());
        imageRepository.save(image);

        return newFilename;
    }

    @Override
    public String deleteImageByFileName(String[] fileNames) throws IOException {
        for (String fileName : fileNames) {
            Optional<Image> optionalImage = imageRepository.findByImageFileName(fileName);

            if (optionalImage.isPresent()) {
                Image image = optionalImage.get();

                Path path = Paths.get(uploadDir + fileName);
                Files.deleteIfExists(path);

                imageRepository.delete(image);
            } else {
                System.out.println("Image not found: " + fileName);
            }
        }
        return "Images deleted successfully.";
    }

}
