package com.example.url_shortener.config;

import com.example.url_shortener.exceptionhandling.CustomAccessDeniedHandler;
import com.example.url_shortener.exceptionhandling.CustomBasicAuthenticationEntryPoint;
import com.example.url_shortener.filter.AuthoritiesLoggingAfterFilter;
import com.example.url_shortener.filter.AuthoritiesLoggingAtFilter;
import com.example.url_shortener.filter.JwtClaimsLoggingAfterFilter;
import com.example.url_shortener.filter.RequestValidationBeforeFilter;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
public class ProjectSecurityConfig {

    private final RsaKeyProperties rsaKeys;

    public ProjectSecurityConfig(RsaKeyProperties rsaKeys) {
        this.rsaKeys = rsaKeys;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.requiresChannel(configurer -> configurer.anyRequest().requiresSecure());
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new CustomJwtGrantedAuthoritiesConverter());
        http.sessionManagement(cs -> cs.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.csrf(AbstractHttpConfigurer::disable);
        http.addFilterBefore(new RequestValidationBeforeFilter(), BasicAuthenticationFilter.class);
        http.addFilterAfter(new AuthoritiesLoggingAfterFilter(), BasicAuthenticationFilter.class);
        http.addFilterAt(new AuthoritiesLoggingAtFilter(), BasicAuthenticationFilter.class);
        http.addFilterAfter(new JwtClaimsLoggingAfterFilter(), BearerTokenAuthenticationFilter.class);
        http.authorizeHttpRequests(configurer ->
                configurer
                        .requestMatchers("/api/urls").authenticated()
                        .requestMatchers("/api/shorten").hasAnyRole("USER", "PREMIUM")
                        .requestMatchers("/api/shorten/custom").hasRole("PREMIUM")
                        .requestMatchers("/api/{shortUrl}", "/register").permitAll()
                        .requestMatchers("/user").authenticated()
                        .requestMatchers("/token").authenticated()
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/v3/api-docs/**").permitAll()
        );
        http.oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter)));
        http.httpBasic(hbc -> hbc.authenticationEntryPoint(new CustomBasicAuthenticationEntryPoint()));
        http.formLogin(Customizer.withDefaults());
        http.exceptionHandling(ehc -> ehc.accessDeniedHandler(new CustomAccessDeniedHandler()));
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(rsaKeys.publicKey()).build();
    }

    @Bean
    JwtEncoder jwtEncoder() {
        JWK jwk = new RSAKey.Builder(rsaKeys.publicKey()).privateKey(rsaKeys.privateKey()).build();
        JWKSource<SecurityContext> jwkSource = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwkSource);
    }

}
