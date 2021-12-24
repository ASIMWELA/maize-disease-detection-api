package com.detection.maize.disease.user.entity;


import com.detection.maize.disease.commons.BaseEntity;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

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
    String password;

    @Column(name="is_active", length = 10)
    boolean isActive;
}
