package com.example.projectbackend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imageId;

    private String imageFileName; // Tên tệp
    private String name;           // Tên mô tả
    private Long referenceId;      // ID tham chiếu
    private String imagePath;      // Đường dẫn đến hình ảnh
    private LocalDate uploadDate;  // Ngày tải lên
}
