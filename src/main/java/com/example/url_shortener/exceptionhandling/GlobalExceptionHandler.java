package com.example.url_shortener.exceptionhandling;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ProblemDetail handleResponseStatusException(ResponseStatusException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(e.getStatusCode(), e.getReason());
        problemDetail.setTitle("Validation Error");
        return problemDetail;
    }

    @ExceptionHandler(UsernameExistsException.class)
    public ProblemDetail handleUsernameExistsException(UsernameExistsException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(e.getStatusCode(), e.getReason());
        problemDetail.setTitle("Duplicate Username");
        problemDetail.setProperty("timestamp", LocalDateTime.now());
        return problemDetail;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGenericException(Exception e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        problemDetail.setTitle("Internal Server Error - " + e.getMessage());
        return problemDetail;
    }

}
