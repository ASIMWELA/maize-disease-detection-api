package com.detection.maize.disease.cnnmodel;

import com.detection.maize.disease.disease.payload.CnnModelSecondProbableDisease;
import com.detection.maize.disease.disease.payload.GetDiseaseResponse;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CnnModelResponse {
    GetDiseaseResponse firstDisease;
    CnnModelSecondProbableDisease secondDisease;
}
