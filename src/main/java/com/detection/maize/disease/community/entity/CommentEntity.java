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

@Entity
@Table(name="comments_table")
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentEntity extends BaseEntity {
    @Column(name="comment_content", length = 800, nullable = false)
    String commentContent;

    @Column(name="comment_likes", length = 200)
    long commentLikes;

    @Column(name="comment_dislikes", length = 200)
    long commentDislikes;

    @Column(name="created_at", length = 200, nullable = false)
    LocalDate createdAt;

    @Column(name="modified_at", length = 200, nullable = false)
    LocalDate modifiedAt;

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
