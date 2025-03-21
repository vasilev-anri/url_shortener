package com.example.url_shortener.repository;

import com.example.url_shortener.entity.Url;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UrlRepository extends JpaRepository<Url, Integer> {
    Optional<Url> findByShortUrl(String shortUrl);

    @Query("SELECT u FROM Url u WHERE u.user.username = :username")
    List<Url> findByUserUsername(@Param("username") String username);
}
