package com.example.projectbackend.bean.request;

import com.example.projectbackend.entity.Contact;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactRequest {
    private Long userId;
    @NotNull(message = "Message must not be blank")
    private String message;

    private Contact.ContactStatus status;
}
