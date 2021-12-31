package com.detection.maize.disease.disease;

import com.detection.maize.disease.commons.ApiResponse;
import com.detection.maize.disease.disease.entity.DiseaseEntity;
import com.detection.maize.disease.disease.entity.PrescriptionEntity;
import com.detection.maize.disease.disease.entity.SymptomEntity;
import com.detection.maize.disease.disease.heteos.DiseaseModel;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface DiseaseService {
    ResponseEntity<ApiResponse> saveDisease(DiseaseEntity disease);
    ResponseEntity<ApiResponse> addDiseaseSymptoms(String diseaseUuid, List<SymptomEntity> symptoms);
    ResponseEntity<ApiResponse> addDiseasePrescriptions(String diseaseUuid, List<PrescriptionEntity> prescriptions);
    ResponseEntity<PagedModel<?>> getAllDiseases(int page, int size, PagedResourcesAssembler<DiseaseEntity> pagedResourcesAssembler);
}
