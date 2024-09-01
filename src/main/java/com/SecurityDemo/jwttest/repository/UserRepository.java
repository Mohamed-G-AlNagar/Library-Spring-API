package com.SecurityDemo.jwttest.repository;

import com.SecurityDemo.jwttest.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository <User, Integer> {
    Optional<User> findByEmail(String email);
}
