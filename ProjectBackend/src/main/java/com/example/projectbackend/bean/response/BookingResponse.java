package com.example.projectbackend.bean.response;

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
public class BookingResponse {
    private Long bookingId;

    private User user;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDateTime updatedAt;

    private List<BookingDetail> bookingDetails;
}
