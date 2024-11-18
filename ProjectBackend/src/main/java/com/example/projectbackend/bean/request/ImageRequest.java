package com.example.projectbackend.bean.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageRequest {

    @NotBlank(message = "Image file name is required.")
    private String imageFileName;

    @NotBlank(message = "Name is required.")
    private String name;

    @NotNull(message = "Reference ID is required.")
    private Long referenceId;

}
