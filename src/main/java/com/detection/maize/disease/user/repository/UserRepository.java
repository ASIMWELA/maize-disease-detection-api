package com.detection.maize.disease.user.repository;

import com.detection.maize.disease.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByUuid(String userUuid);
    boolean existsByEmail(String email);
}
