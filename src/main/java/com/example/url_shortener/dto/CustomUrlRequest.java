package com.example.url_shortener.dto;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.URL;

public class CustomUrlRequest {

    @NotNull(message = "Original Url can not be null")
    @URL(message = "Url must be valid")
    private String originalUrl;

    @NotNull(message = "Custom url can not be null")
    private String customShortUrl;

    public @NotNull(message = "Original Url can not be null") @URL(message = "Url must be valid") String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(@NotNull(message = "Original Url can not be null") @URL(message = "Url must be valid") String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public @NotNull(message = "Custom url can not be null") String getCustomShortUrl() {
        return customShortUrl;
    }

    public void setCustomShortUrl(@NotNull(message = "Custom url can not be null") String customShortUrl) {
        this.customShortUrl = customShortUrl;
    }
}
