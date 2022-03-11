package com.detection.maize.disease.disease.payload;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CnnModelSecondProbableDisease {
    String diseaseUuid;
    String diseaseName;
    String accuracy;
    List<String> symptoms;
    List<String> prescriptions;
}
