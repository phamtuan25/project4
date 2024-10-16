package com.example.projectbackend.controller;

import com.example.projectbackend.bean.request.BookingRequest;
import com.example.projectbackend.bean.request.UserRequest;
import com.example.projectbackend.bean.response.BookingResponse;
import com.example.projectbackend.bean.response.UserResponse;
import com.example.projectbackend.entity.Booking;
import com.example.projectbackend.entity.User;
import com.example.projectbackend.service.BookingService;
import com.example.projectbackend.service.UserService;
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
    public User createUser(@RequestBody UserRequest userRequest) {
        return userService.createUser(userRequest);
    }

    @PutMapping("/{userId}")
    public User updateUser(@PathVariable Long userId, @RequestBody User user) {
        return userService.updateUser(userId, user);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
    }
}
