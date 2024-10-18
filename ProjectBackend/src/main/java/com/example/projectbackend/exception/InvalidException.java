package com.example.projectbackend.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class InvalidException extends RuntimeException {
    private final String key;
    private final String message;
}
