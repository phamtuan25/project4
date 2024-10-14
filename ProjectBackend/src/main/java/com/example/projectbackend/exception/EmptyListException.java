package com.example.projectbackend.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmptyListException extends RuntimeException {
    private final String message;
}
