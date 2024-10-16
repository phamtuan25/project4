package com.example.projectbackend.bean.request;

import com.example.projectbackend.entity.BookingDetail;
import com.example.projectbackend.entity.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequest {
    private User user;
}
