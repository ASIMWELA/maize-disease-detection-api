package com.detection.maize.disease.disease.repositoy;

import com.detection.maize.disease.disease.entity.DiseaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DiseaseRepository extends JpaRepository<DiseaseEntity, Long> {
    Optional<DiseaseEntity> findByDiseaseName(String diseaseName);
    Optional<DiseaseEntity> findByUuid(String diseaseUuid);
    boolean existsByDiseaseName(String diseaseName);
}
