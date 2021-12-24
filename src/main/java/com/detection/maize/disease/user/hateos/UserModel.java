package com.detection.maize.disease.user.hateos;

import com.detection.maize.disease.role.Role;
import com.detection.maize.disease.user.entity.UserEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Relation(itemRelation = "user", collectionRelation = "users")
@JsonPropertyOrder({"uuid", "email","firstName", "lastName","active", "roles"})
public class UserModel extends RepresentationModel<UserModel> {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String firstName;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String lastName;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String uuid;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String email;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    List<Role> roles;
    boolean isActive;

    public static UserModel buildUserModel(UserEntity userEntity) {
        return UserModel.builder()
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName())
                .email(userEntity.getEmail())
                .uuid(userEntity.getUuid())
                .roles(userEntity.getRoles())
                .isActive(userEntity.isActive())
                .build();
    }
}
