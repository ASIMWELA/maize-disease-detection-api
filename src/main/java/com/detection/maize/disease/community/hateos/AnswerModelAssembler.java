package com.detection.maize.disease.community.hateos;

import com.detection.maize.disease.community.entity.AnswerEntity;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class AnswerModelAssembler implements RepresentationModelAssembler<AnswerEntity, AnswerModel> {
    @Override
    public AnswerModel toModel(AnswerEntity entity) {
        return AnswerModel.buildAnswerModel(entity);
    }

    @Override
    public CollectionModel<AnswerModel> toCollectionModel(Iterable<? extends AnswerEntity> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities);
    }
}
