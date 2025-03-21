package com.example.url_shortener.rest;

import com.example.url_shortener.dto.CustomUrlRequest;
import com.example.url_shortener.dto.UrlMapper;
import com.example.url_shortener.dto.UrlResponse;
import com.example.url_shortener.repository.UrlRepository;
import com.example.url_shortener.service.UrlService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class UrlRestController {

    private final UrlService urlService;
    private final UrlRepository urlRepository;

    @Autowired
    public UrlRestController(UrlService urlService, UrlRepository urlRepository) {
        this.urlService = urlService;
        this.urlRepository = urlRepository;
    }

    @GetMapping("/urls")
    public List<UrlResponse> findAll() {
//        return urlService.findAll();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        return urlRepository.findByUserUsername(username).stream().map(UrlMapper::toUrlResponse).collect(Collectors.toList());
//        return urlRepository.findAll().stream().map(UrlMapper::toUrlResponse).collect(Collectors.toList());

    }

    @PostMapping("/shorten")
    public String shorten(@RequestBody String url) throws MalformedURLException {
        return urlService.generateShortUrl(url);
    }

    @GetMapping("/{shortUrl}")
    public ResponseEntity<Object> redirectToOriginalUrl(@PathVariable String shortUrl) {
        return urlRepository.findByShortUrl(shortUrl).map(url -> {
            url.setAccessCount(url.getAccessCount() + 1);
            urlRepository.save(url);
            return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(url.getOriginalUrl())).build();
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/shorten/custom")
    public ResponseEntity<String> shortenCustom(@RequestBody @Valid CustomUrlRequest body) {
        try {
            String originalUrl = body.getOriginalUrl();
            String customShortUrl = body.getCustomShortUrl();
            String shortUrl = urlService.generateCustomShortUrl(originalUrl, customShortUrl);
            return ResponseEntity.ok(shortUrl);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }


}
