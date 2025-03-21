package com.example.url_shortener.rest;

import com.example.url_shortener.entity.AppUser;
import com.example.url_shortener.repository.UserRepository;
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

    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody AppUser user) {
        try {
            String hashPwd = passwordEncoder.encode(user.getPassword());
            user.setPassword(hashPwd);
            AppUser savedUser = userRepository.save(user);

            if (savedUser.getUserId() > 0) {
                return ResponseEntity.status(HttpStatus.CREATED).body("User details registered successfully");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User registration failed");
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An exception has occurred: " + e.getMessage());
        }
    }

}
