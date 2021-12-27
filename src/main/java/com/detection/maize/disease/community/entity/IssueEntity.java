package com.detection.maize.disease.community.entity;

import com.detection.maize.disease.commons.BaseEntity;
import com.detection.maize.disease.user.entity.UserEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name="issues_table")
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class IssueEntity extends BaseEntity {
    @Column(name="issue_name", length = 200, nullable = false, unique = true)
    String issueName;

    @Column(name="issue_description", length = 800, nullable = false, unique = true)
    String issueDescription;

    @Column(name="created_at", nullable = false)
    LocalDate createdAt;

    @Column(name="modified_at", nullable = false)
    LocalDate modifiedAt;

    @Lob
    @Column(name="issue_image")
    private byte[] issueImage;

    @Column(name = "image_name", unique = true)
    private String imageName;

    @Column(name="image_type")
    private String imageType;

    @Column(name="comment_likes", length = 200)
    long commentLikes;

    @Column(name="comment_dislikes", length = 200)
    long commentDislikes;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    @LazyCollection(LazyCollectionOption.FALSE)
    @JsonIgnore
    UserEntity user;

    @OneToMany(mappedBy = "issue")
    @LazyCollection(LazyCollectionOption.FALSE)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonIgnore
    List<CommentEntity> comments;
}
