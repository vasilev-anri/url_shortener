package com.example.url_shortener.dto;

import com.example.url_shortener.entity.AppUser;

public class AppUserDto {

    private String username;
    private boolean enabled;

    public AppUserDto(AppUser user) {
        this.username = user.getUsername();
        this.enabled = user.isEnabled();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
