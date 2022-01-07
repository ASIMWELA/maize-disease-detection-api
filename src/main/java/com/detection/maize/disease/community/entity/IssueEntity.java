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
import java.util.ArrayList;
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
    String question;

    @Column(name="issue_description", length = 2000, nullable = false, unique = true)
    String questionDescription;

    @Column(name="crop", length = 300)
    String crop;

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

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    @LazyCollection(LazyCollectionOption.FALSE)
    @JsonIgnore
    UserEntity user;

    @OneToMany(mappedBy = "issue")
    @LazyCollection(LazyCollectionOption.FALSE)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonIgnore
    List<AnswerEntity> answers;

    @ManyToMany
    @LazyCollection(LazyCollectionOption.TRUE)
    @JsonIgnore
    @JoinTable(
            name="user_issue_votes_table",
            joinColumns = @JoinColumn(name="user_id"),
            inverseJoinColumns = @JoinColumn(name="issue_id"))
    List<UserEntity> issueVotes = new ArrayList<>();

    @ManyToMany
    @LazyCollection(LazyCollectionOption.TRUE)
    @JsonIgnore
    @JoinTable(
            name="user_issue_dislikes_table",
            joinColumns = @JoinColumn(name="user_id"),
            inverseJoinColumns = @JoinColumn(name="issue_id"))
    List<UserEntity> issueDislikes = new ArrayList<>();
}
