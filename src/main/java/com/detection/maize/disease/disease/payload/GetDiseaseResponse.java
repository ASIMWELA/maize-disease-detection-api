package com.detection.maize.disease.disease.payload;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetDiseaseResponse extends RepresentationModel<GetDiseaseResponse> {
    String diseaseUuid;
    String diseaseName;
    List<String> symptoms;
    List<String> prescriptions;
}
