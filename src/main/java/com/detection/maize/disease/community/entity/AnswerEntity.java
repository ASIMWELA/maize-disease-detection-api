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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    @Lob
    @Column(name="answer_image")
    private byte[] answerImage;

    @Column(name = "answer_image_name")
    private String answerImageName;

    @Column(name="answer_image_type")
    private String answerImageType;

    @Column(name="created_at", length = 200, nullable = false)
    Date createdAt;

    @Column(name="modified_at", length = 200, nullable = false)
    Date modifiedAt;

    @ManyToOne
    @JoinColumn(name = "issue_id", nullable = false, updatable = false)
    @LazyCollection(LazyCollectionOption.FALSE)
    @JsonIgnore
    IssueEntity issue;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    @LazyCollection(LazyCollectionOption.FALSE)
    @JsonIgnore
    UserEntity user;

    @ManyToMany
    @LazyCollection(LazyCollectionOption.TRUE)
    @JsonIgnore
    @JoinTable(
            name="user_answer_likes_table",
            joinColumns = @JoinColumn(name="user_id"),
            inverseJoinColumns = @JoinColumn(name="answer_id"))
    List<UserEntity> answerLikes = new ArrayList<>();

    @ManyToMany
    @LazyCollection(LazyCollectionOption.TRUE)
    @JsonIgnore
    @JoinTable(
            name="user_answer_dislikes_table",
            joinColumns = @JoinColumn(name="user_id"),
            inverseJoinColumns = @JoinColumn(name="answer_id"))
    List<UserEntity> answerDislikes = new ArrayList<>();

    //helper functions for updating votes on issues
    public void addAnswerLike(UserEntity user) {
        answerLikes.add(user);
        user.getAnswerLikes().add(this);
    }
    public void removeAnswerLike(UserEntity user) {
        answerLikes.remove(user);
        user.getAnswerLikes().remove(this);
    }

    public void addAnswerDislike(UserEntity user) {
        answerDislikes.add(user);
        user.getAnswerDislikes().add(this);
    }
    public void removeAnswerDislike(UserEntity user) {
        answerDislikes.remove(user);
        user.getAnswerDislikes().remove(this);
    }

}
