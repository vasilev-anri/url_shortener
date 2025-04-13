package com.example.url_shortener.service;

import com.example.url_shortener.dto.UserRegisterRequest;
import com.example.url_shortener.entity.AppUser;

public interface AppUserService {

    AppUser registerUser(UserRegisterRequest registerRequest);

}
