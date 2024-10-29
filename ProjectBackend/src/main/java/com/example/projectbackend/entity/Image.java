package com.example.projectbackend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "images")
@Data
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long imageId;

    @Column(name = "image_file_name")
    private String imageFileName;

    @Column(name = "name")
    private String name;

    @Column(name = "reference_id")
    private Long referenceId;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @ManyToOne
    @JoinColumn(name = "provision_id")
    private Provision provision; // Add this line to link to Provision
}
