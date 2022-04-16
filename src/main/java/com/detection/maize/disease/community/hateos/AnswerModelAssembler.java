package com.detection.maize.disease.community.hateos;

import com.detection.maize.disease.community.entity.AnswerEntity;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

/**
 * @author Augutine Simwela
 *
 * A Helper class for transforming an entity to a hateoas model
 */
@Component
public class AnswerModelAssembler implements RepresentationModelAssembler<AnswerEntity, AnswerModel> {

    /**
     * Maps an answer entity into a DTO
     *
     * @param entity of class {@link  AnswerEntity}
     *
     * @return AnswerModel
     */
    @Override
    public AnswerModel toModel(AnswerEntity entity) {
        return AnswerModel.buildAnswerModel(entity);
    }

    /**
     * Creates a list of DTO from a list of entites
     *
     * @param entities iterables of AnswerEntity
     *
     * @return a Collection of {@link AnswerModel}
     */
    @Override
    public CollectionModel<AnswerModel> toCollectionModel(Iterable<? extends AnswerEntity> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities);
    }
}
