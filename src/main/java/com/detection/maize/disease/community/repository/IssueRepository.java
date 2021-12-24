package com.detection.maize.disease.community.repository;

import com.detection.maize.disease.community.entity.IssueEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IssueRepository extends JpaRepository<IssueEntity, Long> {
    Optional<IssueEntity> findByIssueName(String issueName);
    Optional<IssueEntity>  findByUuid(String issueUuid);
}
