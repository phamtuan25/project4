package com.example.projectbackend.service;

import com.example.projectbackend.bean.request.ImageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@Service
public interface ImageService {
    String saveImages(MultipartFile file, ImageRequest imageRequest) throws IOException;
}
