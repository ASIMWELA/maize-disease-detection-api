package com.detection.maize.disease.cnnmodel;

import com.detection.maize.disease.disease.payload.GetDiseaseResponse;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface CnnModelService {
    MultiLayerNetwork loadModel();
    ResponseEntity<CnnModelResponse> detectDisease(MultipartFile image);
}
