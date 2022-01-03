package com.detection.maize.disease.disease.impl;

import com.detection.maize.disease.commons.ApiResponse;
import com.detection.maize.disease.commons.Constants;
import com.detection.maize.disease.disease.DiseaseController;
import com.detection.maize.disease.disease.DiseaseService;
import com.detection.maize.disease.disease.entity.DiseaseEntity;
import com.detection.maize.disease.disease.entity.PrescriptionEntity;
import com.detection.maize.disease.disease.entity.SymptomEntity;
import com.detection.maize.disease.disease.heteos.*;
import com.detection.maize.disease.disease.payload.AddPrescriptionRequest;
import com.detection.maize.disease.disease.payload.AddSymptomRequest;
import com.detection.maize.disease.disease.payload.GetDiseaseResponse;
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
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.core.EmbeddedWrapper;
import org.springframework.hateoas.server.core.EmbeddedWrappers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DiseaseServiceImpl implements DiseaseService {
    DiseaseRepository diseaseRepository;
    SymptomRepository symptomRepository;
    PrescriptionRepository prescriptionRepository;
    DiseaseModelAssembler diseaseModelAssembler;
    SymptomModelAssembler symptomModelAssembler;
    PrescriptionsModelAssembler prescriptionsModelAssembler;

    public DiseaseServiceImpl(DiseaseRepository diseaseRepository,
                              SymptomRepository symptomRepository,
                              PrescriptionRepository prescriptionRepository,
                              DiseaseModelAssembler diseaseModelAssembler,
                              SymptomModelAssembler symptomModelAssembler,
                              PrescriptionsModelAssembler prescriptionsModelAssembler) {
        this.diseaseRepository = diseaseRepository;
        this.symptomRepository = symptomRepository;
        this.prescriptionRepository = prescriptionRepository;
        this.diseaseModelAssembler = diseaseModelAssembler;
        this.symptomModelAssembler = symptomModelAssembler;
        this.prescriptionsModelAssembler = prescriptionsModelAssembler;
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
            if (diseaseSym.isEmpty()) {
                SymptomEntity symptomEntity = SymptomEntity.builder().symptomDescription(symptom.getSymptomDescription())
                        .uuid(UuidGenerator.generateRandomString(12))
                        .disease(disease)
                        .build();
                symptomRepository.save(symptomEntity);
            } else {
                for (SymptomEntity sym : diseaseSym) {
                    //check is the symptom is already added
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

    @Override
    public ResponseEntity<ApiResponse> addDiseasePrescriptions(String diseaseUuid, AddPrescriptionRequest prescriptions) {
        DiseaseEntity disease = diseaseRepository.findByUuid(diseaseUuid).orElseThrow(
                () -> new EntityNotFoundException("No disease found with the given identifier")
        );
        if (prescriptions.getPrescriptions() == null) {
            throw new OperationNotAllowedException("Prescriptions cannot be empty");
        }

        if (prescriptions.getPrescriptions().isEmpty()) {
            throw new OperationNotAllowedException("Prescriptions cannot be empty");
        }
        List<PrescriptionEntity> diseasePresc = disease.getPrescriptions();

        for (PrescriptionEntity prescription : prescriptions.getPrescriptions()) {
            if (diseasePresc.isEmpty()) {
                PrescriptionEntity prescriptionEntity = PrescriptionEntity.builder()
                        .diseasePrescription(prescription.getDiseasePrescription())
                        .uuid(UuidGenerator.generateRandomString(12))
                        .disease(disease)
                        .build();
                prescriptionRepository.save(prescriptionEntity);
            } else {
                for (PrescriptionEntity presc : diseasePresc) {
                    //check is the prescription is already added
                    if (presc.getDiseasePrescription().equals(prescription.getDiseasePrescription())) {
                        throw new OperationNotAllowedException("Symptom already added");
                    }
                }
                PrescriptionEntity prescriptionEntity2 = PrescriptionEntity.builder()
                        .diseasePrescription(prescription.getDiseasePrescription())
                        .uuid(UuidGenerator.generateRandomString(12))
                        .disease(disease)
                        .build();
                prescriptionRepository.save(prescriptionEntity2);
            }
        }
        return ResponseEntity.ok(ApiResponse.builder().success(true).message("Prescriptions added").build());
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

    @Override
    public ResponseEntity<GetDiseaseResponse> getDisease(String diseaseUuid) {
        DiseaseEntity disease = diseaseRepository.findByUuid(diseaseUuid).orElseThrow(
                () -> new EntityNotFoundException("No disease found with the given identifier")
        );
        List<String> symptoms = disease.getSymptoms().stream().map(SymptomEntity::getSymptomDescription).collect(Collectors.toList());
        List<String> prescriptions = disease.getPrescriptions().stream().map(PrescriptionEntity::getDiseasePrescription).collect(Collectors.toList());
        GetDiseaseResponse getDiseaseResponse =
                GetDiseaseResponse.builder()
                        .diseaseName(disease.getDiseaseName())
                        .diseaseUuid(disease.getUuid())
                        .prescriptions(prescriptions)
                        .symptoms(symptoms)
                        .build();
        getDiseaseResponse.add(linkTo(methodOn(DiseaseController.class).getDiseases(Constants.PAGE, Constants.SIZE, null)).withRel("diseases"));
        return ResponseEntity.ok(getDiseaseResponse);
    }

    @Override
    public ResponseEntity<CollectionModel<?>> getDiseaseSymptoms(String diseaseUuid) {
        DiseaseEntity disease = diseaseRepository.findByUuid(diseaseUuid).orElseThrow(
                () -> new EntityNotFoundException("No disease with the given identifier")
        );
        List<SymptomEntity> symptoms = disease.getSymptoms();
        if (symptoms.isEmpty()) {
            //return an empty list: hateos defaults to returning nothing
            EmbeddedWrappers wrappers = new EmbeddedWrappers(false);
            EmbeddedWrapper wrapper = wrappers.emptyCollectionOf(SymptomsModel.class);
            CollectionModel<Object> symptomsModel = new CollectionModel<>(Collections.singletonList(wrapper));
            symptomsModel.add(linkTo(methodOn(DiseaseController.class).getDiseases(Constants.PAGE, Constants.SIZE, null)).withRel("diseases"));            return ResponseEntity.ok(symptomsModel);
        }
        return ResponseEntity.ok(symptomModelAssembler.toCollectionModel(symptoms).add(linkTo(methodOn(DiseaseController.class).getDiseases(Constants.PAGE, Constants.SIZE, null)).withRel("diseases")));
    }

    @Override
    public ResponseEntity<CollectionModel<?>> getDiseasePrescriptions(String diseaseUuid) {
        DiseaseEntity disease = diseaseRepository.findByUuid(diseaseUuid).orElseThrow(
                () -> new EntityNotFoundException("No disease found with the given identifier")
        );
        List<PrescriptionEntity> prescriptions = disease.getPrescriptions();

        if (prescriptions.isEmpty()) {
            //return an empty list: hateos defaults to returning nothing
            EmbeddedWrappers wrappers = new EmbeddedWrappers(false);
            EmbeddedWrapper wrapper = wrappers.emptyCollectionOf(PrescriptionModel.class);
            CollectionModel<Object> prescriptionsModel = new CollectionModel<>(Collections.singletonList(wrapper));
            return ResponseEntity.ok(prescriptionsModel.add(linkTo(methodOn(DiseaseController.class).getDiseases(Constants.PAGE, Constants.SIZE, null)).withRel("diseases")));
        }
        return ResponseEntity.ok(prescriptionsModelAssembler.toCollectionModel(prescriptions).add(linkTo(methodOn(DiseaseController.class).getDiseases(Constants.PAGE, Constants.SIZE, null)).withRel("diseases")));
    }
}
