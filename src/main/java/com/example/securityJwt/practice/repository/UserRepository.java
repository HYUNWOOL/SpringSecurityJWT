package com.example.securityJwt.practice.repository;

import com.example.securityJwt.practice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByEmail(String id);

  List<User> findByCreatedDateBetween(LocalDateTime start, LocalDateTime end);
}
