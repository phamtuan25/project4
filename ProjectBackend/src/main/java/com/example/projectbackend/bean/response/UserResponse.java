package com.example.projectbackend.bean.response;

import com.example.projectbackend.entity.Booking;
import com.example.projectbackend.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long userId;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String address;
    private User.RoleType role;
    private List<Booking> bookings;

}
