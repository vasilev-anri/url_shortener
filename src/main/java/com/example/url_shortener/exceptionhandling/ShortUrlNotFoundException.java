package com.example.url_shortener.exceptionhandling;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ShortUrlNotFoundException extends ResponseStatusException {
    public ShortUrlNotFoundException(String shortUrl) {
        super(HttpStatus.NOT_FOUND, String.format("ShortUrl '%s' does not exist", shortUrl));
    }
}
