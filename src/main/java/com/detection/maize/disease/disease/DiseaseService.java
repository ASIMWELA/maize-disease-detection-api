package com.detection.maize.disease.disease;

import com.detection.maize.disease.commons.ApiResponse;
import com.detection.maize.disease.disease.entity.DiseaseEntity;
import com.detection.maize.disease.disease.heteos.DiseaseModel;
import com.detection.maize.disease.disease.payload.AddPrescriptionRequest;
import com.detection.maize.disease.disease.payload.AddSymptomRequest;
import com.detection.maize.disease.disease.payload.GetDiseaseResponse;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;

public interface DiseaseService {
    ResponseEntity<ApiResponse> saveDisease(DiseaseEntity disease);
    ResponseEntity<ApiResponse> addDiseaseSymptoms(String diseaseUuid, AddSymptomRequest symptoms);
    ResponseEntity<ApiResponse> addDiseasePrescriptions(String diseaseUuid, AddPrescriptionRequest prescriptions);
    ResponseEntity<PagedModel<?>> getAllDiseases(int page, int size, PagedResourcesAssembler<DiseaseEntity> pagedResourcesAssembler);
    ResponseEntity<GetDiseaseResponse> getDisease(String diseaseUuid);
    ResponseEntity<CollectionModel<?>> getDiseaseSymptoms(String diseaseUuid);
    ResponseEntity<CollectionModel<?>> getDiseasePrescriptions(String diseaseUuid);
}
