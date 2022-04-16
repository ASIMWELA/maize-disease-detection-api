package com.detection.maize.disease.cnnmodel;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Augustine Simwela
 *
 * ModelController. Defines the endpoints for the inference process
 *
 * Uses the injected {@link CnnModelService}
 *
 * @see CnnModelService
 */
@RestController
@RequestMapping("/api/v1/model")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name="Model Controller", description="A user sends an image of a diseaded leaf for analysis")
public class ModelController {
    /**
      * The main dependency of the class
      * App the implementation of the logic in the methods is provided in {@link com.detection.maize.disease.cnnmodel.impl.CnnModelServiceImpl}
     */
    CnnModelService modelService;
    /**
     * Creates an object of this class
     *
     * @param modelService of {@link CnnModelService } class
     */
    public ModelController(CnnModelService modelService) {
        this.modelService = modelService;
    }

    /**
     * An end point for conducting inference
     *
     * @param image of {@link MultipartFile } class
     *
     * @return the top two probable diseases and wrappes them into {@link ResponseEntity} of {@link CnnModelResponse}
     * */
    @PostMapping("/detect")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary="Receives an image of a disased leaf and returns the result of inference")
    public ResponseEntity<CnnModelResponse> detectDisease(@RequestParam("image") MultipartFile image){
        return modelService.detectDisease(image);
    }
}
