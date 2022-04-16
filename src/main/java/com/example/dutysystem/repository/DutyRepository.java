package com.example.dutysystem.repository;

import com.example.dutysystem.entity.Duty;
import com.example.dutysystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DutyRepository extends JpaRepository<Duty, Long> {
    @Query(value = "select duty.* from users join duty_users join duty on duty_users.duty_id = duty.id on users.id = duty_users.users_id where users.id=:user_id", nativeQuery = true)
    List<Duty> findByUser(@Param("user_id") Long userId);

}
