package com.detection.maize.disease.community.hateos;

import com.detection.maize.disease.community.entity.IssueEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDate;

import static lombok.AccessLevel.PRIVATE;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = PRIVATE)
@JsonPropertyOrder(
        {"uuid",
        "question",
        "crop",
        "createdBy",
        "createdAt",
        "issueLikes",
        "issueDislikes",
        "issueAnswers",
        "issueDescription"}
        )
@Relation(itemRelation = "issue", collectionRelation = "issues")
public class IssueModel extends RepresentationModel<IssueModel> {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String question;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String uuid;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String createdBy;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String questionDescription;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String crop;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    LocalDate createdAt;
    long issueLikes;
    long issueDislikes;
    long issueAnswers;
    public static IssueModel build(IssueEntity entity){
        String createdBy = entity.getUser().getFirstName() + " " + entity.getUser().getLastName();
        return IssueModel.builder()
                .issueDislikes(entity.getIssueDislike())
                .issueLikes(entity.getIssueLikes())
                .question(entity.getQuestion())
                .createdBy(createdBy)
                .crop(entity.getCrop())
                .issueAnswers(entity.getAnswers().size())
                .questionDescription(entity.getQuestionDescription())
                .uuid(entity.getUuid())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
