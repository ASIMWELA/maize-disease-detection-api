package com.detection.maize.disease.user.hateos;

import com.detection.maize.disease.user.entity.UserEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserModel extends RepresentationModel<UserModel> {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String firstName;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String lastName;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String uuid;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String email;
    boolean isActive;

    public static UserModel buildUserModel(UserEntity userEntity) {
        return UserModel.builder()
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName())
                .email(userEntity.getEmail())
                .uuid(userEntity.getUuid())
                .isActive(userEntity.isActive())
                .build();
    }
}
