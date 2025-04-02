package com.example.url_shortener.rest;

import com.example.url_shortener.dto.AppUserDto;
import com.example.url_shortener.entity.AppUser;
import com.example.url_shortener.repository.UserRepository;
import com.example.url_shortener.service.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AppUserService userService;

    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder, AppUserService userService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<AppUserDto> registerUser(@RequestBody AppUser user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        AppUser savedUser = userService.registerUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(new AppUserDto(savedUser));
    }

}
