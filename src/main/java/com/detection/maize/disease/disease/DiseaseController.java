package com.detection.maize.disease.disease;

import com.detection.maize.disease.commons.ApiResponse;
import com.detection.maize.disease.disease.entity.DiseaseEntity;
import com.detection.maize.disease.disease.payload.AddPrescriptionRequest;
import com.detection.maize.disease.disease.payload.AddSymptomRequest;
import com.detection.maize.disease.disease.payload.GetDiseaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Diseases Controller", description = "Affords adding diseases, symptoms , prescriptions and retrieving")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DiseaseController {
    DiseaseService diseaseService;

    public DiseaseController(DiseaseService diseaseService) {
        this.diseaseService = diseaseService;
    }

    @PostMapping
    @Operation(summary = "Adds a disease to the database")
    public ResponseEntity<ApiResponse> saveDisease(@Valid @RequestBody DiseaseEntity disease) {
        return diseaseService.saveDisease(disease);
    }

    @GetMapping
    @Operation(summary = "Get paginated diseases")
    public ResponseEntity<PagedModel<?>> getDiseases(
            @PositiveOrZero @RequestParam(value = "page", defaultValue = "0") int page,
            @Positive @RequestParam(value = "size", defaultValue = "20") int size,
            PagedResourcesAssembler<DiseaseEntity> pagedResourceAssembler
    ) {
        return diseaseService.getAllDiseases(page, size, pagedResourceAssembler);
    }

    @PostMapping("/symptoms/{diseaseUuid}")
    @Transactional
    @Operation(summary = "Add symptoms of a particular disease")
    public ResponseEntity<ApiResponse> addDiseaseSymptoms(@PathVariable String diseaseUuid, @RequestBody AddSymptomRequest symptoms) {
        return diseaseService.addDiseaseSymptoms(diseaseUuid, symptoms);
    }

    @PostMapping("/prescriptions/{diseaseUuid}")
    @Operation(summary = "Add prescriptions of a particular disease")
    public ResponseEntity<ApiResponse> addDiseasePrescription(@PathVariable String diseaseUuid, @RequestBody AddPrescriptionRequest prescriptions) {
        return diseaseService.addDiseasePrescriptions(diseaseUuid, prescriptions);
    }

    @GetMapping("/get-symptoms/{diseaseUuid}")
    @Operation(summary = "Get disease symptoms.Given the disease uuid, return the symptoms if the disease is found in the database otherwise throw 404 exception")
    public ResponseEntity<CollectionModel<?>> getDiseaseSymptoms(@PathVariable("diseaseUuid") String diseaseUuid){
        return diseaseService.getDiseaseSymptoms(diseaseUuid);
    }

    @GetMapping("/get-prescriptions/{diseaseUuid}")
    @Operation(summary = "Get a disease prescriptions. Given a disease uuid, return the prescriptions of that particular disease if the disease is found otherwise throw 404")
    public ResponseEntity<CollectionModel<?>> getDiseasePrescriptions(@PathVariable("diseaseUuid") String diseaseUuid){
        return diseaseService.getDiseasePrescriptions(diseaseUuid);
    }
    @GetMapping("/{diseaseUuid}")
    @Operation(summary = "Get a single disease with its symptoms and prescriptions")
    public ResponseEntity<GetDiseaseResponse> getDisease(@PathVariable("diseaseUuid") String diseaseUuid){
        return diseaseService.getDisease(diseaseUuid);
    }
}
