package com.detection.maize.disease.community.entity;

import com.detection.maize.disease.commons.BaseEntity;
import com.detection.maize.disease.user.entity.UserEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name="answers_table")
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AnswerEntity extends BaseEntity {
    @Column(name="answer_content", length = 800, nullable = false)
    String answerContent;

    @Column(name="answer_likes", length = 200)
    long answerLikes;

    @Column(name="answer_dislikes", length = 200)
    long answerDislikes;

    @Column(name="created_at", length = 200, nullable = false)
    Date createdAt;

    @Column(name="modified_at", length = 200, nullable = false)
    Date modifiedAt;

    @ManyToOne
    @JoinColumn(name = "issue_id", nullable = false, updatable = false)
    @LazyCollection(LazyCollectionOption.FALSE)
    @JsonIgnore
    IssueEntity issue;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    @LazyCollection(LazyCollectionOption.FALSE)
    @JsonIgnore
    UserEntity user;

}
