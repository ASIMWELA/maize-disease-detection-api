package com.detection.maize.disease.cnnmodel;


import com.detection.maize.disease.disease.payload.GetDiseaseResponse;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/model")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ModelController {
    CnnModelService modelService;
    public ModelController(CnnModelService modelService) {
        this.modelService = modelService;
    }

    @PostMapping("/detect")
    public ResponseEntity<CnnModelResponse> detectDisease(@RequestParam("image") MultipartFile image){
        return modelService.detectDisease(image);
    }
}
