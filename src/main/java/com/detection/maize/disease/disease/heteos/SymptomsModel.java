package com.detection.maize.disease.disease.heteos;

import com.detection.maize.disease.disease.entity.SymptomEntity;
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
@Relation(itemRelation = "symptom", collectionRelation = "symptoms")
public class SymptomsModel extends RepresentationModel<SymptomsModel> {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String uuid;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String symptomDescription;

    public static SymptomsModel buildSymptomModel(SymptomEntity entity) {
        return SymptomsModel.builder()
                .uuid(entity.getUuid())
                .symptomDescription(entity.getSymptomDescription())
                .build();
    }
}
