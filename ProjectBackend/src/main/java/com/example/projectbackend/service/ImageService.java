package com.example.projectbackend.service;

import com.example.projectbackend.bean.request.ImageRequest;
import com.example.projectbackend.bean.response.ImageResponse;
import com.example.projectbackend.entity.Image;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ImageService {
    public List<Image> getImages(String name, Long referenceId);
    public Image createImage(Image image);
    public void deleteImage(Long imageId);
    ImageResponse saveImage(ImageRequest imageRequest);
}
