package com.example.projectbackend.service.impl;

import com.example.projectbackend.bean.request.ImageRequest;
import com.example.projectbackend.bean.response.ImageResponse;
import com.example.projectbackend.entity.Image;
import com.example.projectbackend.repository.ImageRepository;
import com.example.projectbackend.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    private final ImageRepository imageRepository;
    @Override
    public List<Image> getImages(String name, Long referenceId) {
        return imageRepository.findAllByNameAndReferenceId(name, referenceId);
    }

    @Override
    public Image createImage(Image image) {
        return null;
    }

    @Override
    public void deleteImage(Long imageId) {

    }

    @Override
    public ImageResponse saveImage(ImageRequest imageRequest) {
        Image image = new Image();
        image.setName(imageRequest.getName());
        image.setImageFileName(imageRequest.getImage());
        image.setReferenceId(imageRequest.getReferenceId());
        Image savedImage = imageRepository.save(image);

        return new ImageResponse(
                savedImage.getImageId(),
                savedImage.getImageFileName(),
                savedImage.getName(),
                savedImage.getReferenceId()
        );
    }

}
