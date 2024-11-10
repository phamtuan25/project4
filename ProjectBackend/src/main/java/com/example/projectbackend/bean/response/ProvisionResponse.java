package com.example.projectbackend.bean.response;

import com.example.projectbackend.entity.Image;
import com.example.projectbackend.entity.Provision;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProvisionResponse {
    private Long provisionId;
    private String provisionName;
    private String description;
    private Double price;
    private Provision.ProvisionStatus status;
    private List<String> images;
}