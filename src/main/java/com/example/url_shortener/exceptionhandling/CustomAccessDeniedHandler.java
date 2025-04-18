package com.example.url_shortener.exceptionhandling;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    public CustomAccessDeniedHandler() {
        this.objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }


    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        String message = (accessDeniedException != null && accessDeniedException.getMessage() != null) ? accessDeniedException.getMessage() : "Authorization failed";
        String path = request.getRequestURI();
        String currentTimeStamp = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);

        response.setHeader("error-reason", "Authorization failed");
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType("application/json; charset=utf-8");

        Map<String , Object> errorResponse = new HashMap<>();

        errorResponse.put("timestamp", currentTimeStamp);
        errorResponse.put("status", HttpStatus.FORBIDDEN.value());
        errorResponse.put("error", HttpStatus.FORBIDDEN.getReasonPhrase());
        errorResponse.put("message", message);
        errorResponse.put("path", path);

        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
