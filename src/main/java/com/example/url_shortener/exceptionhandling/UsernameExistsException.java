package com.example.url_shortener.exceptionhandling;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UsernameExistsException extends ResponseStatusException {
    public UsernameExistsException(String username) {
        super(HttpStatus.CONFLICT, String.format("Username '%s' already exists", username));
    }
}
