package com.detection.maize.disease.community.entity;

import com.detection.maize.disease.commons.BaseEntity;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class FileSystemImage extends BaseEntity {
    @Column(name="image_type", nullable = false)
    String imageType;

    @Column (name="image_name", nullable = false)
    String imageName;
    @OneToOne(targetEntity = IssueEntity.class,optional = false)
    @JoinColumn(name = "issue_id")
    IssueEntity issue;

    @OneToOne(targetEntity = AnswerEntity.class,optional = false)
    @JoinColumn(name = "answer_id")
    AnswerEntity answer;
}
