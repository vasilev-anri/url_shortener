package com.example.url_shortener.exceptionhandling;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class CustomShortUrlAlreadyExistsException extends ResponseStatusException {
    public CustomShortUrlAlreadyExistsException(String customShortUrl) {
        super(HttpStatus.CONFLICT, String.format("Custom short url '%s' already exists", customShortUrl));
    }
}

