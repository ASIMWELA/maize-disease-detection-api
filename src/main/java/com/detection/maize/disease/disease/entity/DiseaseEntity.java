package com.detection.maize.disease.disease.entity;

import com.detection.maize.disease.commons.BaseEntity;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="diseases_table")
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DiseaseEntity extends BaseEntity {
    @Column(name="disease_name", unique = true, length = 200, updatable = false)
    String diseaseName;
}
