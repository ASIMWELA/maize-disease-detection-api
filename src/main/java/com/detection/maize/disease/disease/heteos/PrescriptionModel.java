package com.detection.maize.disease.disease.heteos;

import com.detection.maize.disease.disease.entity.PrescriptionEntity;
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
@Relation(itemRelation = "prescription", collectionRelation = "prescriptions")
public class PrescriptionModel extends RepresentationModel<PrescriptionModel> {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String uuid;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    String diseasePrescription;
    public static PrescriptionModel buildPrescriptionModel(PrescriptionEntity entity){
        return PrescriptionModel.builder()
                .uuid(entity.getUuid())
                .diseasePrescription(entity.getDiseasePrescription())
                .build();
    }
}
