package com.detection.maize.disease.disease;

import com.detection.maize.disease.commons.ApiResponse;
import com.detection.maize.disease.disease.entity.DiseaseEntity;
import com.detection.maize.disease.disease.payload.AddPrescriptionRequest;
import com.detection.maize.disease.disease.payload.AddSymptomRequest;
import com.detection.maize.disease.disease.payload.GetDiseaseResponse;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping("/api/v1/diseases")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DiseaseController {
    DiseaseService diseaseService;

    public DiseaseController(DiseaseService diseaseService) {
        this.diseaseService = diseaseService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse> saveDisease(@Valid @RequestBody DiseaseEntity disease) {
        return diseaseService.saveDisease(disease);
    }

    @GetMapping
    public ResponseEntity<PagedModel<?>> getDiseases(
            @PositiveOrZero @RequestParam(value = "page", defaultValue = "0") int page,
            @Positive @RequestParam(value = "size", defaultValue = "20") int size,
            PagedResourcesAssembler<DiseaseEntity> pagedResourceAssembler
    ) {
        return diseaseService.getAllDiseases(page, size, pagedResourceAssembler);
    }

    @PostMapping("/symptoms/{diseaseUuid}")
    @Transactional
    public ResponseEntity<ApiResponse> addDiseaseSymptoms(@PathVariable String diseaseUuid, @RequestBody AddSymptomRequest symptoms) {
        return diseaseService.addDiseaseSymptoms(diseaseUuid, symptoms);
    }

    @PostMapping("/prescriptions/{diseaseUuid}")
    public ResponseEntity<ApiResponse> addDiseasePrescription(@PathVariable String diseaseUuid, @RequestBody AddPrescriptionRequest prescriptions) {
        return diseaseService.addDiseasePrescriptions(diseaseUuid, prescriptions);
    }

    @GetMapping("/get-symptoms/{diseaseUuid}")
    public ResponseEntity<CollectionModel<?>> getDiseaseSymptoms(@PathVariable("diseaseUuid") String diseaseUuid){
        return diseaseService.getDiseaseSymptoms(diseaseUuid);
    }

    @GetMapping("/get-prescriptions/{diseaseUuid}")
    public ResponseEntity<CollectionModel<?>> getDiseasePrescriptions(@PathVariable("diseaseUuid") String diseaseUuid){
        return diseaseService.getDiseasePrescriptions(diseaseUuid);
    }
    @GetMapping("/{diseaseUuid}")
    public ResponseEntity<GetDiseaseResponse> getDisease(@PathVariable("diseaseUuid") String diseaseUuid){
        return diseaseService.getDisease(diseaseUuid);
    }
}
