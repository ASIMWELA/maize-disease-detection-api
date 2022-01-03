package com.detection.maize.disease.disease.heteos;

import com.detection.maize.disease.disease.entity.SymptomEntity;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class SymptomModelAssembler implements RepresentationModelAssembler<SymptomEntity, SymptomsModel> {
    @Override
    public SymptomsModel toModel(SymptomEntity entity) {
        return SymptomsModel.buildSymptomModel(entity);
    }

    @Override
    public CollectionModel<SymptomsModel> toCollectionModel(Iterable<? extends SymptomEntity> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities);
    }
}
