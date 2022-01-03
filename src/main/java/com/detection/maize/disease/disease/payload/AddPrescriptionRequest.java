package com.detection.maize.disease.disease.payload;

import com.detection.maize.disease.disease.entity.PrescriptionEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddPrescriptionRequest {
    @JsonProperty("prescriptions")
    @NotEmpty(message = "prescriptions cannot be empty")
    List<PrescriptionEntity> prescriptions;
}
