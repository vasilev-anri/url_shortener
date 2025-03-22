package com.example.url_shortener.service;

import com.example.url_shortener.entity.Url;
import com.example.url_shortener.entity.AppUser;
import com.example.url_shortener.repository.UrlRepository;
import com.example.url_shortener.repository.UserRepository;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class UrlServiceImpl implements UrlService {

    private final UrlRepository urlRepository;
    private final UserRepository userRepository;
    private final JdbcTemplate jdbcTemplate;

    public UrlServiceImpl(UrlRepository urlRepository, UserRepository userRepository, JdbcTemplate jdbcTemplate) {
        this.urlRepository = urlRepository;
        this.userRepository = userRepository;
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public List<Url> findAll() {
        return urlRepository.findAll();
    }

    @Override
    public Url findById(int id) {
        Optional<Url> res = urlRepository.findById(id);
        Url url = null;
        if (res.isPresent()) {
            url = res.get();
        } else {
            throw new RuntimeException("Url with id " + id + " not found");
        }
        return url;
    }

    @Override
    public Url save(Url url) {
        return urlRepository.save(url);
    }

    @Override
    public void deleteById(int id) {
        urlRepository.deleteById(id);
    }

    @Override
    public String generateShortUrl(String originalUrl) throws MalformedURLException {

        if (!isValidUrl(originalUrl)) {
            throw new MalformedURLException("Invalid URL");
        }

        String shortUrl = generateUniqueShortUrl();
        saveUrl(originalUrl, shortUrl);

        return shortUrl;
    }

    private AppUser getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        return userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public String generateCustomShortUrl(String originalUrl, String customShortUrl) throws MalformedURLException {
        if (!isValidUrl(originalUrl)) {
            throw new MalformedURLException("Invalid URL");
        }
        if (!isValidCustomShortUrl(customShortUrl)) {
            throw new MalformedURLException("Invalid Custom URL");
        }

        if (urlRepository.findByShortUrl(customShortUrl).isPresent()) {
            throw new MalformedURLException("Custom URL already exists");
        }

        saveUrl(originalUrl, customShortUrl);
        
        return customShortUrl;
    }

    @Override
    @Scheduled(cron = "@midnight")
    public void deleteOldUrls() {
        jdbcTemplate.update("SELECT delete_old_urls()");
    }

    private void saveUrl(String originalUrl, String shortUrl) {
        AppUser user = getAuthenticatedUser();
        Url url = new Url();
        url.setOriginalUrl(originalUrl);
        url.setShortUrl(shortUrl);
        url.setUser(user);
        urlRepository.save(url);
    }


    private String generateUniqueShortUrl() {
        String url = generateRandomShortUrl();
        if (urlRepository.findByShortUrl(url).isPresent()) {
            return generateUniqueShortUrl();
        }
        return url;
    }

    private String generateRandomShortUrl() {
        int length = 8;
        StringBuilder res = new StringBuilder();
        Random random = new Random();

        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        for (int i = 0; i < length; i++) {
            res.append(chars.charAt(random.nextInt(chars.length())));
        }

        return res.toString();
    }

    private boolean isValidCustomShortUrl(String shortUrl) {
        String regex = "^[a-zA-Z0-9_-]+$";
        return shortUrl.matches(regex);
    }

    private boolean isValidUrl(String url) {
        UrlValidator validator = new UrlValidator();
        return validator.isValid(url);
    }


}
