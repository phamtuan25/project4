package com.example.projectbackend.service;

import com.example.projectbackend.bean.request.PasswordRequest;
import com.example.projectbackend.bean.request.UserRequest;
import com.example.projectbackend.bean.response.UserResponse;
import com.example.projectbackend.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    public Page<UserResponse> getAllUsers(Pageable pageable, String keyword);
    public UserResponse getDetailUser(Long userId);
    public User createUser(UserRequest userRequest);
    public User updateUser(Long userId, User user);
    UserResponse changePassword(Long userId, PasswordRequest passwordRequest);
     User getUserName(String email);
}
