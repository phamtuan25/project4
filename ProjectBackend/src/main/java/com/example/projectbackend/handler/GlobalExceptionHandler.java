package com.example.projectbackend.handler;

import com.example.projectbackend.bean.response.ErrorResponse;
import com.example.projectbackend.exception.DuplicateException;
import com.example.projectbackend.exception.EmptyListException;
import com.example.projectbackend.exception.InvalidException;
import com.example.projectbackend.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(EmptyListException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleEmptyListException(EmptyListException emptyListException){
        return new ErrorResponse(HttpStatus.NO_CONTENT.value(), emptyListException.getKey(), emptyListException.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(NotFoundException notFoundException){
        return new ErrorResponse(HttpStatus.NOT_FOUND.value(), notFoundException.getKey(), notFoundException.getMessage());
    }

    @ExceptionHandler(DuplicateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleDuplicateException(DuplicateException duplicateException){
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(),duplicateException.getKey(), duplicateException.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public List<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException e){
        List<ErrorResponse> errors = new ArrayList<>();
        e.getBindingResult().getAllErrors()
                .forEach(error -> {
                    errors.add(new ErrorResponse(
                            HttpStatus.BAD_REQUEST.value(),
                            ((FieldError) error).getField(),
                            error.getDefaultMessage()));
                });
        return errors;  
    }
    @ExceptionHandler(InvalidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleInvalidException(InvalidException e){
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(),e.getKey(), e.getMessage());
    }
}
