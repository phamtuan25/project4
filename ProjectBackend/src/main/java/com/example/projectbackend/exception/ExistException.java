package com.example.projectbackend.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ExistException extends RuntimeException {
  private final String key;
  private final String message;
}
