package com.detection.maize.disease.user.entity;


import com.detection.maize.disease.commons.BaseEntity;
import com.detection.maize.disease.community.entity.AnswerEntity;
import com.detection.maize.disease.community.entity.IssueEntity;
import com.detection.maize.disease.role.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="users_table")
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserEntity extends BaseEntity {
    @Column(name="fist_name", nullable=false, length = 90)
    String firstName;

    @Column(name="last_name", nullable=false, length = 90)
    String lastName;

    @Column(name="email", unique = true, nullable = false, length = 150)
    String email;

    @Column(name="password", length = 200, nullable = false)
    @JsonIgnore
    String password;

    @Column(name="is_active", length = 10)
    boolean isActive;

    @OneToMany(mappedBy = "user")
    @LazyCollection(LazyCollectionOption.FALSE)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonIgnore
    List<IssueEntity> issues;

    @OneToMany(mappedBy = "user")
    @LazyCollection(LazyCollectionOption.FALSE)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonIgnore
    List<AnswerEntity> comments;

    @ManyToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JoinTable(
            name="user_roles_table",
            joinColumns = @JoinColumn(name="user_id"),
            inverseJoinColumns = @JoinColumn(name="role_id"))
    List<Role> roles;

    @ManyToMany(mappedBy = "issueVotes")
    private List<IssueEntity> issueUpVotes = new ArrayList<>();

    @ManyToMany(mappedBy = "issueDownVotes")
    private List<IssueEntity> issueDownVotes = new ArrayList<>();

    @ManyToMany(mappedBy = "answerLikes")
    private List<AnswerEntity> answerLikes = new ArrayList<>();

    @ManyToMany(mappedBy = "answerDislikes")
    private List<AnswerEntity> answerDislikes = new ArrayList<>();


}
