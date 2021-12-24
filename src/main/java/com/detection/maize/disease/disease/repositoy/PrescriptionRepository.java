package com.detection.maize.disease.disease.repositoy;

import com.detection.maize.disease.disease.entity.PrescriptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrescriptionRepository extends JpaRepository<PrescriptionEntity, Long> {
}
