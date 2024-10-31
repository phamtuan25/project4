package com.example.projectbackend.bean.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserBookingResponse {
    private Long userId;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String address;
}
