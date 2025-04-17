package com.example.url_shortener.filter;

import jakarta.servlet.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;


@Slf4j
public class AuthoritiesLoggingAfterFilter implements Filter {
    @Override

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (null != authentication) {
            log.info("User {} successfully authenticated and has the role: {}", authentication.getName(), authentication.getAuthorities().toString());
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

}
