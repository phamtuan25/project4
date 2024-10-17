package com.example.projectbackend.controller;

import com.example.projectbackend.bean.request.LoginRequest;
import com.example.projectbackend.bean.request.UserRequest;
import com.example.projectbackend.bean.response.UserResponse;
import com.example.projectbackend.entity.User;
import com.example.projectbackend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    @GetMapping
    public List<UserResponse> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{userId}")
    public UserResponse getDetailUser(@PathVariable Long userId) {
        return userService.getDetailUser(userId);
    }

    @PostMapping
    public User createUser(@Valid @RequestBody UserRequest userRequest) {
        return userService.createUser(userRequest);
    }

    @PutMapping("/{userId}")
    public User updateUser(@PathVariable Long userId, @RequestBody User user) {
        return userService.updateUser(userId, user);
    }

    @PostMapping("/login")
    public UserResponse login(@Valid @RequestBody LoginRequest loginRequest){
        return userService.Login(loginRequest);
    }
    
}
