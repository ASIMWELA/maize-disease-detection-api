package com.detection.maize.disease.role;

import com.detection.maize.disease.commons.BaseEntity;
import com.detection.maize.disease.enums.ERole;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@Entity
@Table(name = "roles_table")
public class Role extends BaseEntity {
    @Enumerated(EnumType.STRING)
    @NaturalId
    @Column(length = 20)
    ERole name;
}
