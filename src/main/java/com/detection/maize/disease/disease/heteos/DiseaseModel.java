package com.detection.maize.disease.disease.heteos;

import com.detection.maize.disease.disease.entity.DiseaseEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Relation(itemRelation = "disease", collectionRelation = "diseases")
public class DiseaseModel extends RepresentationModel<DiseaseModel> {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String uuid;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String diseaseName;
    public static DiseaseModel buildDiseaseModel(DiseaseEntity diseaseEntity) {
        return DiseaseModel.builder()
                .uuid(diseaseEntity.getUuid())
                .diseaseName(diseaseEntity.getDiseaseName())
                .build();
    }
}
