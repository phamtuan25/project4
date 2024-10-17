package com.example.projectbackend.bean.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProvisionRequest {
    private String provisionName;
    private String description;
    private BigDecimal price;
}