package com.example.projectbackend.bean.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageResponse {
    private Long imageId;
    private String imageFileName;
    private String name;
    private Long referenceId;
}