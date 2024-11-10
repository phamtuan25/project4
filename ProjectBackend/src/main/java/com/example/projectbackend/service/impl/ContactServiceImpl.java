package com.example.projectbackend.service.impl;

import com.example.projectbackend.bean.request.ContactRequest;
import com.example.projectbackend.bean.response.ContactResponse;
import com.example.projectbackend.entity.Contact;
import com.example.projectbackend.entity.User;
import com.example.projectbackend.mapper.ContactMapper;
import com.example.projectbackend.repository.ContactRepository;
import com.example.projectbackend.repository.UserRepository;
import com.example.projectbackend.service.ContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ContactServiceImpl implements ContactService {
    private final ContactRepository contactRepository;
    private final UserRepository userRepository;

    @Override
    public Page<ContactResponse> getAllContacts(@PageableDefault(size = 10, page = 0) Pageable pageable,
                                               @RequestParam(required = false) String keyword) {
        Specification<Contact> spec = searchByKeyword(keyword);
        Page<Contact> contactPage = contactRepository.findAll(spec, pageable);
        return contactPage.map(contact -> {
            ContactResponse contactResponse = ContactMapper.convertToResponse(contact);
            return contactResponse;
        });
    }

    @Override
    public Contact createContact(ContactRequest contactRequest) {
        Contact contact = ContactMapper.convertFromRequest(contactRequest);
        if (contactRequest.getUserId() != null) {
            User user =
                    userRepository.findById(contactRequest.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            contact.setUser(user);
        } else {
            throw new RuntimeException("User ID is required");
        }
        contact.setMessage(contactRequest.getMessage());
        return contactRepository.save(contact);
    }

    @Override
    public Contact updateContact(Long contactId, Contact contact) {
        Contact contactUpdate = contactRepository.findById(contactId).orElse(null);
        if(contactUpdate != null){
            setContact(contactUpdate, contact);
        }
        return contactRepository.save(contactUpdate);
    }

    public void setContact(Contact contactUpdate, Contact contactInput) {
        contactUpdate.setStatus(contactInput.getStatus());
        contactUpdate.setUpdatedAt(LocalDateTime.now());
    }
    public static Specification<Contact> searchByKeyword(String keyword) {
        return (root, query, criteriaBuilder) -> {
            if (keyword == null || keyword.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            String likePattern = "%" + keyword + "%";
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("message").as(String.class)), likePattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("status").as(String.class)), likePattern)
            );
        };
    }
}
