package com.oms.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import com.oms.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}