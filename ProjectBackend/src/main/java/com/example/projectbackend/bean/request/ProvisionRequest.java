package com.example.projectbackend.bean.request;

import com.example.projectbackend.entity.Image;
import com.example.projectbackend.entity.Provision;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProvisionRequest {

    @NotEmpty(message = "Provision name is required")
    private String provisionName;

    @NotEmpty(message = "Description is required")
    private String description;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private BigDecimal price;

    @NotNull(message = "Status is required")
    private Provision.ProvisionStatus status;

    private List<ImageRequest> images;

}
