package com.detection.maize.disease.disease.repositoy;

import com.detection.maize.disease.disease.entity.SymptomEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SymptomRepository extends JpaRepository<SymptomEntity, Long> {
}
