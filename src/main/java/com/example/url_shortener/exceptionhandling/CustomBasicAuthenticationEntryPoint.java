package com.example.url_shortener.exceptionhandling;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class CustomBasicAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    public CustomBasicAuthenticationEntryPoint() {
        this.objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        String message = (authException != null && authException.getMessage() != null) ? authException.getMessage() : "Unauthorized";
        String path = request.getRequestURI();
        String currentTimeStamp = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);

        response.setHeader("error-reason", "Authentication failed");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json; charset=utf-8");

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("timestamp", currentTimeStamp);
        errorResponse.put("status", HttpStatus.UNAUTHORIZED.value());
        errorResponse.put("error", HttpStatus.UNAUTHORIZED.getReasonPhrase());
        errorResponse.put("message", message);
        errorResponse.put("path", path);

        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));

    }

}
