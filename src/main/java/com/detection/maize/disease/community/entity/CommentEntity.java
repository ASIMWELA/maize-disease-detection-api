package com.detection.maize.disease.community.entity;

import com.detection.maize.disease.commons.BaseEntity;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
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

    @Column(name="created_at", length = 200, nullable = false)
    LocalDate createdAt;

    @Column(name="modified_at", length = 200, nullable = false)
    LocalDate modifiedAt;

}
