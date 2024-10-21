package com.example.projectbackend.controller;


import com.example.projectbackend.bean.request.LoginRequest;
import com.example.projectbackend.bean.request.PasswordRequest;
import com.example.projectbackend.bean.request.UserRequest;
import com.example.projectbackend.bean.response.LoginResponse;
import com.example.projectbackend.bean.response.UserResponse;
import com.example.projectbackend.entity.User;
import com.example.projectbackend.service.UserService;
import com.example.projectbackend.util.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Validated
//@PreAuthorize("hasAnyRole('ADMIN')")
public class UserController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
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
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtUtil.generateJwtToken(authentication);
        return ResponseEntity.ok(new LoginResponse(token, loginRequest.getEmail()));
        //return userService.Login(loginRequest);
    }
    @PutMapping("/changePassword/{userId}")
    public UserResponse changePassword(@PathVariable Long userId,@Valid @RequestBody PasswordRequest passwordRequest){
        return userService.changePassword(userId,passwordRequest);
    }
}
