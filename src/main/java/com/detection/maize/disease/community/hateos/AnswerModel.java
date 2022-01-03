package com.detection.maize.disease.community.hateos;

import com.detection.maize.disease.community.entity.AnswerEntity;
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
                "answerLikes",
                "answerDislikes",
                "createdAt",
                "answerContent"}
)
@Relation(itemRelation = "answer", collectionRelation = "answers")
public class AnswerModel extends RepresentationModel<AnswerModel> {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String answerContent;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String answerBy;
    long answerLikes;
    long answerDislikes;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String uuid;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    LocalDate createdAt;

    public static AnswerModel buildAnswerModel(AnswerEntity answerEntity) {
        return AnswerModel.builder()
                .answerContent(answerEntity.getAnswerContent())
                .createdAt(answerEntity.getCreatedAt())
                .answerLikes(answerEntity.getAnswerLikes())
                .answerBy(answerEntity.getUser().getFirstName() + " " + answerEntity.getUser().getLastName())
                .answerDislikes(answerEntity.getAnswerDislikes())
                .uuid(answerEntity.getUuid())
                .build();
    }
}
