package com.detection.maize.disease.disease.entity;

import com.detection.maize.disease.commons.BaseEntity;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="prescriptions_table")
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PrescriptionEntity extends BaseEntity {
    @Column(name="disease_prescription", length = 800, unique = true, nullable = false)
    String diseasePrescription;
}
