package com.example.url_shortener.dto;

import com.example.url_shortener.entity.AppUser;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(
        description = """
                Schema to hold user-specific URLs with usage statistics
                """
)
@Data
public class UrlResponse {

    private String originalUrl;
    private String shortUrl;
    private LocalDateTime createdAt = LocalDateTime.now();
    private long accessCount;
    private String username;

}
