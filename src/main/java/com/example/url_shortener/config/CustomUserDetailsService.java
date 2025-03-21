package com.example.url_shortener.config;

import com.example.url_shortener.entity.AppUser;
import com.example.url_shortener.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
//@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User Details not found for the user: " + username));
//        List<GrantedAuthority> authorities = appUser.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getRole())).collect(Collectors.toList());
//        return new User(appUser.getUsername(), appUser.getPassword(), authorities);
        Hibernate.initialize(appUser.getRoles());

        List<GrantedAuthority> authorities = appUser.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getRole()))
                .collect(Collectors.toList());
        return new User(appUser.getUsername(), appUser.getPassword(), authorities);
    }

}
