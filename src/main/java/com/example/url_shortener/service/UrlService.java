package com.example.url_shortener.service;

import com.example.url_shortener.entity.Url;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.util.List;


public interface UrlService {

    List<Url> findAll();
    Url findById(int id);
    Url save(Url url);
    void deleteById(int id);
    String generateShortUrl(String originalUrl) throws MalformedURLException;
    String generateCustomShortUrl(String originalUrl, String customShortUrl) throws MalformedURLException;
    void deleteOldUrls();
    Url incrementAccessCount(String shorUrl);


}
