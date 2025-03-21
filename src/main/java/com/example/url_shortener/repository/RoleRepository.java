package com.example.url_shortener.repository;

import com.example.url_shortener.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
