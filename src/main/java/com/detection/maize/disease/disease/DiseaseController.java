package com.detection.maize.disease.disease;

import com.detection.maize.disease.commons.ApiResponse;
import com.detection.maize.disease.community.entity.IssueEntity;
import com.detection.maize.disease.disease.entity.DiseaseEntity;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
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
}
