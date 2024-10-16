package com.example.projectbackend.mapper;

import com.example.projectbackend.bean.request.BookingRequest;
import com.example.projectbackend.bean.request.UserRequest;
import com.example.projectbackend.bean.response.BookingResponse;
import com.example.projectbackend.bean.response.UserResponse;
import com.example.projectbackend.entity.Booking;
import com.example.projectbackend.entity.User;

public class UserMapper {
    public static UserResponse convertToResponse(User user){
        UserResponse userResponse = new UserResponse();
        userResponse.setFullName(user.getFullName());
        userResponse.setEmail(user.getEmail());
        userResponse.setPhoneNumber(user.getPhoneNumber());
        userResponse.setAddress(user.getAddress());
        userResponse.setRole(user.getRole());
        userResponse.setBookings(user.getBookings());
        return userResponse;
    }

    public static User convertFromRequest(UserRequest userRequest){
        User user = new User();
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setEmail(userRequest.getEmail());
        user.setPhoneNumber(userRequest.getPhoneNumber());
        user.setAddress(userRequest.getAddress());
        user.setPassword(userRequest.getPassword());
        user.setRole(userRequest.getRole());
        return user;
    }
}
