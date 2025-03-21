package com.example.url_shortener.entity;

import jakarta.persistence.*;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "url")
@ToString
public class Url {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 512)
    private String originalUrl;

    @Column(nullable = false, unique = true)
    private String shortUrl;

    private LocalDateTime createdAt = LocalDateTime.now();

    private long accessCount;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    public AppUser getUser() {
        return user;
    }

    public void setUser(AppUser user) {
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public long getAccessCount() {
        return accessCount;
    }

    public void setAccessCount(long accessCount) {
        this.accessCount = accessCount;
    }
}
