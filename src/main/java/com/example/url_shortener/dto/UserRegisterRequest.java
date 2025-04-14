package com.example.url_shortener.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Schema(
        name = "CreateUserRequest",
        description = "Request payload for user registration"
)
@Data
@AllArgsConstructor
public class UserRegisterRequest {

    @NotEmpty(message = "Username can not be null or empty")
    @Size(min = 6, max = 56, message = "Username must be 6-56 characters long")
    private String username;

    @NotEmpty(message = "Password can not be null or empty")
    @Size(max = 120, message = "Maximum password length: 120 characters")
    private String password;

}
