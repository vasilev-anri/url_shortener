package com.example.url_shortener.service;

import com.example.url_shortener.entity.AppUser;
import com.example.url_shortener.exceptionhandling.UsernameExistsException;
import com.example.url_shortener.repository.UserRepository;
import org.springframework.stereotype.Service;


@Service
public class AppUserServiceImpl implements AppUserService {

    private final UserRepository userRepository;

    public AppUserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public AppUser registerUser(AppUser user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new UsernameExistsException(user.getUsername());
        }
        return userRepository.save(user);
    }

}
