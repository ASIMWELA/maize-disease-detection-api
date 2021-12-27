package com.detection.maize.disease.community.hateos;

import com.detection.maize.disease.community.entity.IssueEntity;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class IssueModelAssembler implements RepresentationModelAssembler<IssueEntity, IssueModel> {
    @Override
    public IssueModel toModel(IssueEntity entity) {
        return IssueModel.build(entity);
    }

    @Override
    public CollectionModel<IssueModel> toCollectionModel(Iterable<? extends IssueEntity> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities);
    }
}
