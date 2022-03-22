package com.detection.maize.disease.cnnmodel;

import com.detection.maize.disease.disease.payload.CnnModelSecondProbableDisease;
import com.detection.maize.disease.disease.payload.GetDiseaseResponse;
import lombok.*;

/**
 *@author Augustine Simwela
CnnModelResponse
 a container class that returns the top two highly likely diseases
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CnnModelResponse {
    GetDiseaseResponse firstDisease;
    CnnModelSecondProbableDisease secondDisease;
}
