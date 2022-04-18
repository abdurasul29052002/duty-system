package com.example.dutysystem.repository;

import com.example.dutysystem.entity.Complete;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompleteRepository extends JpaRepository<Complete,Long> {
    boolean existsByUserIdAndDutyId(Long user_id, Long duty_id);
}
