package com.detection.maize.disease.community.entity;

import com.detection.maize.disease.commons.BaseEntity;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import java.time.LocalDate;

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
}
