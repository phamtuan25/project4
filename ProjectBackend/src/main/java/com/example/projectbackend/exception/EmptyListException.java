package com.example.projectbackend.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class EmptyListException extends RuntimeException {
    private final String key;
    private final String message;
}
