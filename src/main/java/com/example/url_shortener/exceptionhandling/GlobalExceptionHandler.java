package com.example.url_shortener.exceptionhandling;

import org.springframework.http.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.MalformedURLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            fieldErrors.put(error.getField(), error.getDefaultMessage());
        });

        Map<String, Object> errorBody = new HashMap<>();
        errorBody.put("title", "Validation Failed");
        errorBody.put("status", HttpStatus.BAD_REQUEST.value());
        errorBody.put("details", fieldErrors);
        errorBody.put("timestamp", LocalDateTime.now());

        return new ResponseEntity<>(errorBody, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGenericException(Exception e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        problemDetail.setTitle("Internal Server Error - " + e.getMessage());
        return problemDetail;
    }


    @ExceptionHandler(MalformedURLException.class)
    public ProblemDetail handleMalformedURLException(MalformedURLException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
        problemDetail.setTitle("Invalid URL Format");
        problemDetail.setProperty("timestamp", LocalDateTime.now());
        return problemDetail;
    }

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

    @ExceptionHandler(CustomShortUrlAlreadyExistsException.class)
    public ProblemDetail handleCustomShortUrlAlreadyExistsException(CustomShortUrlAlreadyExistsException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(e.getStatusCode(), e.getReason());
        problemDetail.setTitle("Duplicate ShortUrl");
        problemDetail.setProperty("timestamp", LocalDateTime.now());
        return problemDetail;
    }

    @ExceptionHandler(ShortUrlNotFoundException.class)
    public ProblemDetail handleShortUrlNotFoundException(ShortUrlNotFoundException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(e.getStatusCode(), e.getReason());
        problemDetail.setTitle("ShortUrl Not Found");
        problemDetail.setProperty("timestamp", LocalDateTime.now());
        return problemDetail;
    }


}
