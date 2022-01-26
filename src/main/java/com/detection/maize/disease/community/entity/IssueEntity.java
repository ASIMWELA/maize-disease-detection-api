package com.detection.maize.disease.community.entity;

import com.detection.maize.disease.commons.BaseEntity;
import com.detection.maize.disease.community.enums.EIssueStatus;
import com.detection.maize.disease.user.entity.UserEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
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
    @Column(name="issue_name", length = 200, nullable = false)
    String question;

    @Column(name="issue_description", length = 2000, nullable = false)
    String questionDescription;

    @Column(name="crop", length = 300)
    String crop;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    EIssueStatus issueStatus;

    @Column(name="created_at", nullable = false)
    Date createdAt;

    @Column(name="modified_at", nullable = false)
    Date modifiedAt;

    @Lob
    @Column(name="issue_image")
    private byte[] issueImage;

    @Column(name = "image_name")
    private String imageName;

    @Column(name="image_type")
    private String imageType;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    @LazyCollection(LazyCollectionOption.FALSE)
    @JsonIgnore
    UserEntity user;

    @OneToMany(mappedBy = "issue")
    @LazyCollection(LazyCollectionOption.FALSE)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonIgnore
    List<AnswerEntity> answers = new ArrayList<>();

    @ManyToMany
    @LazyCollection(LazyCollectionOption.TRUE)
    @JsonIgnore
    @JoinTable(
            name="user_issue_likes_table",
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
    List<UserEntity> issueDownVotes = new ArrayList<>();

    //helper functions for updating votes on issues
    public void addIssueVote(UserEntity user) {
        issueVotes.add(user);
        user.getIssueUpVotes().add(this);
    }
    public void removeIssueVote(UserEntity user) {
        issueVotes.remove(user);
        user.getIssueUpVotes().remove(this);
    }

    public void addIssueDownVote(UserEntity user) {
        issueDownVotes.add(user);
        user.getIssueDownVotes().add(this);
    }
    public void removeIssueDownVote(UserEntity user) {
        issueDownVotes.remove(user);
        user.getIssueDownVotes().remove(this);
    }
}
