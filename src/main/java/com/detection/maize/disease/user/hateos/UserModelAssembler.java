package com.detection.maize.disease.user.hateos;

import com.detection.maize.disease.user.entity.UserEntity;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class UserModelAssembler implements RepresentationModelAssembler<UserEntity, UserModel> {
    @Override
    public UserModel toModel(UserEntity entity) {
        return UserModel.buildUserModel(entity);
    }
    @Override
    public CollectionModel<UserModel> toCollectionModel(Iterable<? extends UserEntity> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities);
    }
}
