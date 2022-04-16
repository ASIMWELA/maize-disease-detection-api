package com.detection.maize.disease.cnnmodel;

import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Augustine Simwela
 *
 * Defines the contact for implementing the CnnModelServic
 */
public interface CnnModelService {

    /**
     * contract method for loading a multilayer model
     * @return {@link MultiLayerNetwork}
     */
    MultiLayerNetwork loadModel();

    /**
     * Contract for to be implemented to return the top two probable diseases
     * @param image of class {@link MultipartFile}
     * @return top two diseases
     */
    ResponseEntity<CnnModelResponse> detectDisease(MultipartFile image);
}
