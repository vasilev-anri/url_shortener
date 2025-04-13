package com.example.url_shortener.rest;

import com.example.url_shortener.dto.AppUserDto;
import com.example.url_shortener.dto.UserRegisterRequest;
import com.example.url_shortener.entity.AppUser;
import com.example.url_shortener.repository.UserRepository;
import com.example.url_shortener.service.AppUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;


@Tag(
        name = "User Management API",
        description = """
                Provides operations for User management:
                - User registration
                - Fetch user details after login
                """
)
@RestController
@RequestMapping(path = "/api", produces = {MediaType.APPLICATION_JSON_VALUE})
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final AppUserService userService;


    @Operation(
            summary = "Register a new User",
            description = """
                    Creates a new User
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "User created successfully"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Such Username already exists",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "type": "about:blank",
                                                "title": "Duplicate Username",
                                                "status": 409,
                                                "detail": "Username 'string' already exists",
                                                "instance": "/api/register",
                                                "timestamp": "timestamp"
                                            }
                                            """
                            )
                    )
            )
    })
    @PostMapping("/register")
    public ResponseEntity<AppUserDto> registerUser(@RequestBody UserRegisterRequest request) {
        AppUser user = userService.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new AppUserDto(user));
    }

    @PostMapping("/user")
    public ResponseEntity<AppUserDto> getUserDetailsAfterLogin(Authentication authentication) {
        Optional<AppUser> user = userRepository.findByUsername(authentication.getName());
        return user.map(appUser -> ResponseEntity.ok(new AppUserDto(appUser))).orElse(ResponseEntity.notFound().build());
    }


}
