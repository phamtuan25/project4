package com.example.projectbackend.repository;

import com.example.projectbackend.bean.response.ImageResponse;
import com.example.projectbackend.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findAllByNameAndReferenceId(String Name, Long referenceId);
}
