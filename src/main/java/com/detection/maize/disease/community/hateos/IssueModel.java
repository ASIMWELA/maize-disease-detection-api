package com.detection.maize.disease.community.hateos;

import com.detection.maize.disease.community.entity.AnswerEntity;
import com.detection.maize.disease.community.entity.IssueEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

import static lombok.AccessLevel.PRIVATE;

/**
 * @author Augustine Simwela
 *
 * An issue  DTO based on HATEAOS which generates self describing links
 * <p>
 * Adds total issues likes and dislikes before returning the DTO
 * <p>
 * Uses {@link JsonPropertyOrder} to arrange the return values in json
 */
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
                "issueStatus",
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
    String issueStatus;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Date createdAt;
    long issueLikes = 0;
    long issueDislikes;
    long issueAnswers;


    /**
     * A helper method to create an issue DTO from an entity
     * <p>
     * Maps {@link IssueEntity} into {@link IssueModel}
     *
     * @param entity of class {@link IssueEntity} that reflects a model that is directly stored on databse
     *
     *@return {@link IssueModel}
     */
    public static IssueModel build(IssueEntity entity) {
        String createdBy = entity.getUser().getFirstName() + " " + entity.getUser().getLastName();
        int numberOfAnswers = 0;
        long issueUpVotes = 0;
        long issueDownVotes = 0;
        if (entity.getAnswers() != null) {
            numberOfAnswers = entity.getAnswers().size();
        }
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
        if (entity.getIssueVotes() != null) {
            issueUpVotes = entity.getIssueVotes().size();
        }
        if (entity.getIssueDownVotes() != null) {
            issueDownVotes = entity.getIssueDownVotes().size();
        }
        return IssueModel.builder()
                .issueLikes(issueUpVotes)
                .issueDislikes(issueDownVotes)
                .question(entity.getQuestion())
                .createdBy(createdBy)
                .issueStatus(entity.getIssueStatus().name())
                .crop(entity.getCrop())
                .issueAnswers(numberOfAnswers)
                .questionDescription(entity.getQuestionDescription())
                .uuid(entity.getUuid())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
