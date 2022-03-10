package com.detection.maize.disease.disease.payload;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CnnModelSecondProbableDisease {
    String uuid, diseaseName, percentage;
}
