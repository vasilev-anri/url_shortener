package com.example.url_shortener.repository;

import com.example.url_shortener.entity.AppUser;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<AppUser, Long> {

    //    Optional<AppUser> findByUsername(String username);
    @Query("SELECT u FROM AppUser u JOIN FETCH u.roles WHERE u.username = :username")
    Optional<AppUser> findByUsername(@Param("username") String username);
}
