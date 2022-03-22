package com.detection.maize.disease.cnnmodel;


import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Augustine Simwela
 * ModelController. Defines the endpoints for the inference process
 * uses the injected @link modelService
 * @see CnnModelService
 */
@RestController
@RequestMapping("/api/v1/model")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ModelController {
    CnnModelService modelService;
    /**
     * Constructor.
     * @param modelService of {@link CnnModelService } class
     */
    public ModelController(CnnModelService modelService) {
        this.modelService = modelService;
    }

    /**
     * Constructor.
     * @param image of {@link MultipartFile } class
     * @return the top two probable diseases using {@link ResponseEntity} of {@link CnnModelResponse}
     * */
    @PostMapping("/detect")
    public ResponseEntity<CnnModelResponse> detectDisease(@RequestParam("image") MultipartFile image){
        return modelService.detectDisease(image);
    }
}
