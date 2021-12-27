package com.detection.maize.disease.disease.entity;

import com.detection.maize.disease.commons.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;

@Entity
@Table(name="symptoms_table")
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SymptomEntity extends BaseEntity {
    @Column(name="symptom_description", unique = true, length = 800, nullable = false)
    String symptomDescription;

    @ManyToOne
    @JoinColumn(name = "disease_id")
    @LazyCollection(LazyCollectionOption.FALSE)
    @JsonIgnore
    DiseaseEntity disease;
}
