package com.detection.maize.disease.community.hateos;

import com.detection.maize.disease.community.entity.IssueEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;

import static lombok.AccessLevel.PRIVATE;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = PRIVATE)
public class IssueModel extends RepresentationModel<IssueModel> {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String issueName;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String uuid;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String createdBy;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String issueDescription;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    LocalDate createdAt;
    long commentLikes;
    long commentDislikes;
    public static IssueModel build(IssueEntity entity){
        String createdBy = entity.getUser().getFirstName() + " " + entity.getUser().getLastName();
        return IssueModel.builder()
                .commentDislikes(entity.getCommentDislikes())
                .commentLikes(entity.getCommentLikes())
                .issueName(entity.getIssueName())
                .createdBy(createdBy)
                .issueDescription(entity.getIssueDescription())
                .uuid(entity.getUuid())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
