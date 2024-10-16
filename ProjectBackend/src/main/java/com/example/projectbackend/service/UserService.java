package com.example.projectbackend.service;

import com.example.projectbackend.bean.request.UserRequest;
import com.example.projectbackend.bean.response.UserResponse;
import com.example.projectbackend.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface UserService {
    public List<UserResponse> getAllUsers();
    public UserResponse getDetailUser(Long userId);
    public User createUser(UserRequest userRequest);
    public User updateUser(Long userId, User user);
    public void deleteUser(Long userId);
}
