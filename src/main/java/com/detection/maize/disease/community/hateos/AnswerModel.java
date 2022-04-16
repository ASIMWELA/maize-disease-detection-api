package com.detection.maize.disease.community.hateos;

import com.detection.maize.disease.community.entity.AnswerEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDate;
import java.util.Date;

import static lombok.AccessLevel.PRIVATE;

/**
 * An issue answer DTO based on HATEAOS which generates self describing links
 * <p>
 * Links generated based on creteria such as if an answer has an associated image or not
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
                "answerLikes",
                "answerDislikes",
                "createdAt",
                "answerContent"}
)
@Relation(itemRelation = "answer", collectionRelation = "answers")
public class AnswerModel extends RepresentationModel<AnswerModel> {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    /**
     * Holds the actual answer as a string
     */
            String answerContent;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    /**
     * Returns the user who created this answer
     */
            String answerBy;

    long answerLikes;
    long answerDislikes;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String uuid;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Date createdAt;

    /**
     * A helper method to create an answer DTO from an entity
     * <p>
     * Maps {@link AnswerEntity} into {@link AnswerModel}
     *
     * @param answerEntity of class {@link AnswerEntity} that reflects a model that is directly stored on databse
     *
     * @return {@link AnswerModel}
     */
    public static AnswerModel buildAnswerModel(AnswerEntity answerEntity) {
        long totalAnswerLike = 0;
        long totalAnswerDisLike = 0;
        if (answerEntity.getAnswerLikes() != null) {
            totalAnswerLike = answerEntity.getAnswerLikes().size();
        }
        if (answerEntity.getAnswerDislikes() != null) {
            totalAnswerDisLike = answerEntity.getAnswerDislikes().size();
        }
        return AnswerModel.builder()
                .answerContent(answerEntity.getAnswerContent())
                .createdAt(answerEntity.getCreatedAt())
                .answerLikes(totalAnswerLike)
                .answerDislikes(totalAnswerDisLike)
                .answerBy(answerEntity.getUser().getFirstName() + " " + answerEntity.getUser().getLastName())
                .uuid(answerEntity.getUuid())
                .build();
    }
}
