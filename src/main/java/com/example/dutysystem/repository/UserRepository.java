package com.example.dutysystem.repository;

import com.example.dutysystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.TreeSet;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUsernameAndEnabled(String username, boolean enabled);

    Optional<User> findByUsername(String username);

    List<User> findAllByEnabled(boolean enabled);

    @Query(value = "select users.* from  users where users.username like %:username%", nativeQuery = true)
    List<User> search(String username);
}
