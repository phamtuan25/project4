package com.example.projectbackend.bean.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class ImageRequest {
    private String name;
    private Long referenceId;
    private String image;
}
