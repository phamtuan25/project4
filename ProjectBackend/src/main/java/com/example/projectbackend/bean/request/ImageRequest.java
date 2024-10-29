package com.example.projectbackend.bean.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class ImageRequest {

    @NotEmpty(message = "Name is required")
    private String name;

    @NotNull(message = "Reference ID is required")
    private Long referenceId;

    @NotEmpty(message = "Image is required")
    private String image; // Assuming this is the image file name or base64 string
}
