package com.detection.maize.disease.cnnmodel;

import com.detection.maize.disease.disease.payload.CnnModelSecondProbableDisease;
import com.detection.maize.disease.disease.payload.GetDiseaseResponse;
import lombok.*;

/**
 *@author Augustine Simwela
 *
 *Wrapper class for returning  the top two highly likely diseases
 *
 * Uses {@link GetDiseaseResponse} for returning trhe first disease and {@link CnnModelSecondProbableDisease} for the second disease
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
