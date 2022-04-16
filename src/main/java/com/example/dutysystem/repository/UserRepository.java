package com.example.dutysystem.repository;

import com.example.dutysystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.TreeSet;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUsernameAndEnabled(String username, boolean enabled);

    Optional<User> findByUsername(String username);

    List<User> findAllByEnabled(boolean enabled);
}
