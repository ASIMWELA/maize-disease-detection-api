package com.detection.maize.disease.community.payload;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AnswerRequest {
    //personal details
    @NotEmpty(message = "answer content cannot be empty")
    String answer;
}
