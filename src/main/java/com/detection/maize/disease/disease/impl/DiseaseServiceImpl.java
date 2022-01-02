package com.detection.maize.disease.disease.impl;

import com.detection.maize.disease.commons.ApiResponse;
import com.detection.maize.disease.disease.DiseaseService;
import com.detection.maize.disease.disease.entity.DiseaseEntity;
import com.detection.maize.disease.disease.entity.PrescriptionEntity;
import com.detection.maize.disease.disease.entity.SymptomEntity;
import com.detection.maize.disease.disease.heteos.DiseaseModel;
import com.detection.maize.disease.disease.heteos.DiseaseModelAssembler;
import com.detection.maize.disease.disease.payload.AddSymptomRequest;
import com.detection.maize.disease.disease.repositoy.DiseaseRepository;
import com.detection.maize.disease.disease.repositoy.PrescriptionRepository;
import com.detection.maize.disease.disease.repositoy.SymptomRepository;
import com.detection.maize.disease.exception.EntityAlreadyExistException;
import com.detection.maize.disease.exception.EntityNotFoundException;
import com.detection.maize.disease.exception.OperationNotAllowedException;
import com.detection.maize.disease.util.UuidGenerator;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DiseaseServiceImpl implements DiseaseService {
    DiseaseRepository diseaseRepository;
    SymptomRepository symptomRepository;
    PrescriptionRepository prescriptionRepository;
    DiseaseModelAssembler diseaseModelAssembler;

    public DiseaseServiceImpl(DiseaseRepository diseaseRepository,
                              SymptomRepository symptomRepository,
                              PrescriptionRepository prescriptionRepository,
                              DiseaseModelAssembler diseaseModelAssembler) {
        this.diseaseRepository = diseaseRepository;
        this.symptomRepository = symptomRepository;
        this.prescriptionRepository = prescriptionRepository;
        this.diseaseModelAssembler = diseaseModelAssembler;
    }

    @Override
    public ResponseEntity<ApiResponse> saveDisease(DiseaseEntity disease) {
        if (diseaseRepository.existsByDiseaseName(disease.getDiseaseName())) {
            throw new EntityAlreadyExistException("Disease already exists");
        }
        disease.setUuid(UuidGenerator.generateRandomString(12));
        diseaseRepository.save(disease);
        return new ResponseEntity<>(ApiResponse.builder().success(true).message("disease saved").build(), HttpStatus.CREATED);
    }

    public ResponseEntity<ApiResponse> addDiseaseSymptoms(String diseaseUuid, AddSymptomRequest symptoms) {
        DiseaseEntity disease = diseaseRepository.findByUuid(diseaseUuid).orElseThrow(
                () -> new EntityNotFoundException("No disease found with the given identifier")
        );
        if (symptoms.getSymptoms() == null) {
            throw new OperationNotAllowedException("Symptoms cannot be empty");
        }
        if (symptoms.getSymptoms().isEmpty()) {
            throw new OperationNotAllowedException("Symptoms cannot be empty");
        }
        List<SymptomEntity> diseaseSym = disease.getSymptoms();

        for (SymptomEntity symptom : symptoms.getSymptoms()) {
            //check is the symptom is already added
            if (diseaseSym.isEmpty()) {
                SymptomEntity symptomEntity = SymptomEntity.builder().symptomDescription(symptom.getSymptomDescription())
                        .uuid(UuidGenerator.generateRandomString(12))
                        .disease(disease)
                        .build();
                symptomRepository.save(symptomEntity);
            } else {
                for (SymptomEntity sym : diseaseSym) {
                    if (sym.getSymptomDescription().equals(symptom.getSymptomDescription())) {
                        throw new OperationNotAllowedException("Symptom already added");
                    }
                }
                SymptomEntity symptomEntity = SymptomEntity.builder().symptomDescription(symptom.getSymptomDescription())
                        .uuid(UuidGenerator.generateRandomString(12))
                        .disease(disease)
                        .build();
                symptomRepository.save(symptomEntity);
            }
        }
        return ResponseEntity.ok(ApiResponse.builder().success(true).message("Symptoms added").build());

    }

    //TODO: create end points
    @Override
    public ResponseEntity<ApiResponse> addDiseasePrescriptions(String diseaseUuid, List<PrescriptionEntity> prescriptions) {
        DiseaseEntity disease = diseaseRepository.findByUuid(diseaseUuid).orElseThrow(
                () -> new EntityNotFoundException("No disease found with the given identifier")
        );
        if (prescriptions.isEmpty()) {
            throw new OperationNotAllowedException("Prescriptions cannot be empty");
        }
        prescriptions.forEach(prescription -> {
            if (disease.getPrescriptions() != null) {
                //check is the symptom is already added
                disease.getPrescriptions().forEach(presc -> {
                    if (presc.getDiseasePrescription().equals(prescription.getDiseasePrescription())) {
                        throw new OperationNotAllowedException("Symptom already added");
                    } else {
                        prescription.setDisease(disease);
                        prescriptionRepository.save(prescription);
                    }
                });
            } else {
                prescription.setDisease(disease);
                prescriptionRepository.save(prescription);
            }
        });
        return ResponseEntity.ok(ApiResponse.builder().success(true).message("Symptoms added").build());
    }

    @Override
    public ResponseEntity<PagedModel<?>> getAllDiseases(int page, int size, PagedResourcesAssembler<DiseaseEntity> pagedResourcesAssembler) {
        Page<DiseaseEntity> diseases = diseaseRepository.findAll(PageRequest.of(page, size));
        if (diseases.hasContent()) {
            return ResponseEntity.ok(pagedResourcesAssembler
                    .toModel(diseases, diseaseModelAssembler));
        }
        return ResponseEntity.ok(pagedResourcesAssembler.toEmptyModel(diseases, DiseaseModel.class));
    }
}
