package com.example.projectbackend.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.function.Supplier;

@Getter
@Setter
@AllArgsConstructor
public class NotFoundException extends RuntimeException {
    private final String key;
    private final String message;
}
