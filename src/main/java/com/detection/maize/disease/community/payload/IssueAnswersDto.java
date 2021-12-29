package com.detection.maize.disease.community.payload;

import com.detection.maize.disease.community.hateos.AnswerModel;
import com.detection.maize.disease.community.hateos.IssueModel;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Collection;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class IssueAnswersDto {
    IssueModel issue;
    Collection<AnswerModel> answers;
}
