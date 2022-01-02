package com.detection.maize.disease.disease.entity;

import com.detection.maize.disease.commons.BaseEntity;
import com.detection.maize.disease.community.entity.IssueEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

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
    @NotEmpty(message = "Disease name cannot be empty")
    String diseaseName;

    @OneToMany(mappedBy = "disease")
    @LazyCollection(LazyCollectionOption.FALSE)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonIgnore
    List<PrescriptionEntity> prescriptions = new ArrayList<>();

    @OneToMany(mappedBy = "disease")
    @LazyCollection(LazyCollectionOption.FALSE)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonIgnore
    List<SymptomEntity> symptoms = new ArrayList<>();
}
