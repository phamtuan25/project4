package com.example.projectbackend.mapper;

import com.example.projectbackend.bean.response.UserBookingResponse;
import com.example.projectbackend.bean.response.UserResponse;
import com.example.projectbackend.entity.User;

public class UserBookingMapper {
    public static UserBookingResponse convertToResponse(User user){
        UserBookingResponse userBookingResponse = new UserBookingResponse();
        userBookingResponse.setUserId(user.getUserId());
        userBookingResponse.setFullName(user.getFullName());
        userBookingResponse.setEmail(user.getEmail());
        userBookingResponse.setPhoneNumber(user.getPhoneNumber());
        userBookingResponse.setAddress(user.getAddress());
        return userBookingResponse;
    }
}
