package com.example.url_shortener.dto;

import com.example.url_shortener.entity.Url;

public class UrlMapper {

    public static UrlResponse toUrlResponse(Url url) {
        UrlResponse response = new UrlResponse();
        response.setId(url.getId());
        response.setOriginalUrl(url.getOriginalUrl());
        response.setShortUrl(url.getShortUrl());
        response.setShortUrl(url.getShortUrl());
        response.setCreatedAt(url.getCreatedAt());
        response.setAccessCount(url.getAccessCount());
        response.setUsername(url.getUser().getUsername());

        return response;

    }

}
