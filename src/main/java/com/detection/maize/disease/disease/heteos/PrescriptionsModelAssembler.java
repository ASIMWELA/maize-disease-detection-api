package com.detection.maize.disease.disease.heteos;

import com.detection.maize.disease.disease.entity.PrescriptionEntity;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class PrescriptionsModelAssembler implements RepresentationModelAssembler<PrescriptionEntity, PrescriptionModel> {
    @Override
    public PrescriptionModel toModel(PrescriptionEntity entity) {
        return PrescriptionModel.buildPrescriptionModel(entity);
    }

    @Override
    public CollectionModel<PrescriptionModel> toCollectionModel(Iterable<? extends PrescriptionEntity> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities);
    }
}
