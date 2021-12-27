package com.detection.maize.disease.disease.entity;

import com.detection.maize.disease.commons.BaseEntity;
import com.detection.maize.disease.user.entity.UserEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;

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

    @ManyToOne
    @JoinColumn(name = "disease_id")
    @LazyCollection(LazyCollectionOption.FALSE)
    @JsonIgnore
    DiseaseEntity disease;
}
