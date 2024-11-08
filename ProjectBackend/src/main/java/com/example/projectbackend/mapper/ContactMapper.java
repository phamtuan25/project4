package com.example.projectbackend.mapper;

import com.example.projectbackend.bean.request.ContactRequest;
import com.example.projectbackend.bean.response.ContactResponse;
import com.example.projectbackend.entity.Contact;

import java.time.LocalDateTime;

public class ContactMapper {
    public static ContactResponse convertToResponse(Contact contact) {
        ContactResponse contactResponse = new ContactResponse();
        contactResponse.setContactId(contact.getContactId());
        contactResponse.setUserResponse(UserMapper.convertToResponse(contact.getUser()));
        contactResponse.setMessage(contact.getMessage());
        contactResponse.setStatus(contact.getStatus());
        contactResponse.setCreatedAt(contact.getCreatedAt());
        contactResponse.setUpdatedAt(contact.getUpdatedAt());
        return contactResponse;
    }
    public static Contact convertFromRequest(ContactRequest contactRequest) {
        Contact contact = new Contact();
        contact.setMessage(contactRequest.getMessage());
        contact.setStatus(Contact.ContactStatus.NEW);
        contact.setCreatedAt(LocalDateTime.now());
        return contact;
    }
}
