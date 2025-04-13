package com.example.url_shortener.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class UserRegisterRequest {
    private String username;
    private String password;
}
