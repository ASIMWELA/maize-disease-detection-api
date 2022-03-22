package com.detection.maize.disease.cnnmodel;

import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Augustine Simwela
 * CnnModelService interface.
 * Defines the contact for implementing the CnnModelService
 * defines the main contracts namely @link loadModel and @link detectDisease
 */
public interface CnnModelService {
    MultiLayerNetwork loadModel();

    /**
     *
     * @param image of class {@link MultipartFile}
     * @return top two diseases
     */
    ResponseEntity<CnnModelResponse> detectDisease(MultipartFile image);
}
