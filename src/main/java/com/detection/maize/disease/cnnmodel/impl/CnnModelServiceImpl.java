package com.detection.maize.disease.cnnmodel.impl;

import com.detection.maize.disease.cnnmodel.CnnModelService;
import com.detection.maize.disease.disease.payload.GetDiseaseResponse;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CnnModelServiceImpl implements CnnModelService {
    //TODO: provide the implementation
    @Override
    public MultiLayerNetwork loadModel() {
        return null;
    }

    @Override
    public ResponseEntity<GetDiseaseResponse> detectDisease(MultipartFile image) {
        return null;
    }
}
