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
    Date createdAt;

    public static AnswerModel buildAnswerModel(AnswerEntity answerEntity) {
        long totalAnswerLike = 0;
        long totalAnswerDisLike = 0;
        if(answerEntity.getAnswerLikes() != null){
            totalAnswerLike = answerEntity.getAnswerLikes().size();
        }
        if(answerEntity.getAnswerDislikes() != null){
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
