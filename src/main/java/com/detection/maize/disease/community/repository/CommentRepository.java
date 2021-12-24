package com.detection.maize.disease.community.repository;

import com.detection.maize.disease.community.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
}
