package com.example.projectbackend.bean.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class ErrorResponse {
    private HttpStatus statusCode;
    private String key;
    private String message;
}
