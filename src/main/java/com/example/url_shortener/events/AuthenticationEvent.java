//package com.example.url_shortener.events;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.context.event.EventListener;
//import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
//import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
//import org.springframework.stereotype.Component;
//
//@Component
//@Slf4j
//public class AuthenticationEvent {
//
//    @EventListener
//    public void onSuccess(AuthenticationSuccessEvent successEvent) {
//        log.info("Authentication success for user: {}", successEvent.getAuthentication().getName());
//    }
//
//    @EventListener
//    public void onFailure(AbstractAuthenticationFailureEvent failureEvent) {
//        log.info("Authentication failed for user: {} due to {}", failureEvent.getAuthentication().getName(),
//                failureEvent.getException().getMessage());
//    }
//
//}
