package com.example.projectbackend.controller;

import com.example.projectbackend.bean.request.ContactRequest;
import com.example.projectbackend.bean.response.ContactResponse;
import com.example.projectbackend.entity.Contact;
import com.example.projectbackend.service.ContactService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/contacts")
public class ContactController {
    private final ContactService contactService;
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE', 'MANAGER')")
    @GetMapping
    public Page<ContactResponse> getAllContacts(Pageable pageable, @RequestParam(required = false) String keyword) {
        return contactService.getAllContacts(pageable,keyword);
    }

    @PostMapping
    public Contact createContact(@Valid @RequestBody ContactRequest contactRequest) {
        return contactService.createContact(contactRequest);
    }

    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE', 'MANAGER')")
    @PutMapping("/{contactId}")
    public Contact updateContact(@PathVariable Long contactId, @RequestBody Contact contact) {
        return contactService.updateContact(contactId, contact);
    }
}
