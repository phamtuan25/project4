package com.example.projectbackend.bean.response;

import com.example.projectbackend.entity.Contact;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactResponse {
    private Long contactId;
    private UserResponse userResponse;
    private String message;
    private Contact.ContactStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
