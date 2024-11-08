package com.example.projectbackend.service;

import com.example.projectbackend.bean.request.ContactRequest;
import com.example.projectbackend.bean.response.ContactResponse;
import com.example.projectbackend.entity.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface ContactService {
    public Page<ContactResponse> getAllContacts(Pageable pageable, String keyword);
    public Contact createContact(ContactRequest contactRequest);
    public Contact updateContact(Long contactId, Contact contact);
}
