package com.example.projectbackend.bean.request;

import com.example.projectbackend.entity.Image;
import com.example.projectbackend.entity.Provision;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProvisionRequest {
    private String provisionName;
    private String description;
    private BigDecimal price;
    private Provision.ProvisionStatus status;
    private List<Image> images;
}