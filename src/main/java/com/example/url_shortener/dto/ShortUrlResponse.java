package com.example.url_shortener.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Schema(
        description = "Schema to hold successfully created short url"
)
@Data @AllArgsConstructor
public class ShortUrlResponse {

    private String shortUrl;

}
