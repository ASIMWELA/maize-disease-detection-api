package com.detection.maize.disease.disease.heteos;

import com.detection.maize.disease.disease.entity.DiseaseEntity;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class DiseaseModelAssembler implements RepresentationModelAssembler<DiseaseEntity, DiseaseModel> {

    @Override
    public DiseaseModel toModel(DiseaseEntity entity) {
        return DiseaseModel.buildDiseaseModel(entity);
    }

    @Override
    public CollectionModel<DiseaseModel> toCollectionModel(Iterable<? extends DiseaseEntity> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities);
    }
}
