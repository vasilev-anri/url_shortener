package com.example.url_shortener.config;

import com.example.url_shortener.exceptionhandling.CustomAccessDeniedHandler;
import com.example.url_shortener.exceptionhandling.CustomBasicAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class ProjectSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.requiresChannel(configurer -> configurer.anyRequest().requiresSecure());
        http.csrf(AbstractHttpConfigurer::disable);
        http.authorizeHttpRequests(configurer ->
                configurer
                        .requestMatchers("/api/urls").authenticated()
                        .requestMatchers("/api/shorten").hasAnyRole("USER", "PREMIUM")
                        .requestMatchers("/api/shorten/custom").hasRole("PREMIUM")
                        .requestMatchers("/api/{shortUrl}", "/register").permitAll()

        );
        http.httpBasic(hbc -> hbc.authenticationEntryPoint(new CustomBasicAuthenticationEntryPoint()));
        http.exceptionHandling(ehc -> ehc.accessDeniedHandler(new CustomAccessDeniedHandler()));
        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}
