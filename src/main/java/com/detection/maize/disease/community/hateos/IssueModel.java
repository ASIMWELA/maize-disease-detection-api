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
        "issueName",
        "createdBy",
        "createdAt",
        "issueLikes",
        "issueDislikes",
        "issueDescription"}
        )
@Relation(itemRelation = "issue", collectionRelation = "issues")
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
    long issueLikes;
    long issueDislikes;
    public static IssueModel build(IssueEntity entity){
        String createdBy = entity.getUser().getFirstName() + " " + entity.getUser().getLastName();
        return IssueModel.builder()
                .issueDislikes(entity.getIssueDislike())
                .issueLikes(entity.getIssueLikes())
                .issueName(entity.getIssueName())
                .createdBy(createdBy)
                .issueDescription(entity.getIssueDescription())
                .uuid(entity.getUuid())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
