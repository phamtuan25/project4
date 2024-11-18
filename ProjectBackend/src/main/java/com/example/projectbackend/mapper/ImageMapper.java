package com.example.projectbackend.mapper;

import com.example.projectbackend.bean.request.ImageRequest;
import com.example.projectbackend.bean.response.ImageResponse;
import com.example.projectbackend.entity.Image;

public class ImageMapper {
    public static ImageResponse convertToResponse(Image image) {
        ImageResponse imageResponse = new ImageResponse();
        imageResponse.setImageId(image.getImageId());
        imageResponse.setImageFileName(image.getImageFileName());
        imageResponse.setName(image.getName());
        imageResponse.setReferenceId(image.getReferenceId());
        return imageResponse;
    }

    public static Image convertFromRequest(ImageRequest imageRequest) {
        Image image = new Image();
        image.setName(imageRequest.getName());
        image.setReferenceId(imageRequest.getReferenceId());
        image.setImageFileName(imageRequest.getImageFileName());
        return image;
    }
}
