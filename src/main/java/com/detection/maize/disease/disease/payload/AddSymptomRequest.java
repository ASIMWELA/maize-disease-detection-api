package com.detection.maize.disease.disease.payload;

import com.detection.maize.disease.disease.entity.SymptomEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddSymptomRequest {
    @JsonProperty("symptoms")
    @NotEmpty(message = "Symptoms cannot be empty")
    List<SymptomEntity> symptoms;
}
