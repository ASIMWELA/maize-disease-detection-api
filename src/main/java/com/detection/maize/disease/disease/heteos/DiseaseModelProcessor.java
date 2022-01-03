package com.detection.maize.disease.disease.heteos;

import com.detection.maize.disease.disease.DiseaseController;
import com.detection.maize.disease.disease.entity.DiseaseEntity;
import com.detection.maize.disease.disease.repositoy.DiseaseRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DiseaseModelProcessor implements RepresentationModelProcessor<DiseaseModel> {
    DiseaseRepository diseaseRepository;

    public DiseaseModelProcessor(DiseaseRepository diseaseRepository) {
        this.diseaseRepository = diseaseRepository;
    }

    @Override
    public DiseaseModel process(DiseaseModel model) {
        DiseaseEntity disease = diseaseRepository.findByUuid(model.getUuid()).get();
        model.add(linkTo(methodOn(DiseaseController.class).getDiseaseSymptoms(disease.getUuid())).withRel("symptoms"));
        model.add(linkTo(methodOn(DiseaseController.class).getDiseasePrescriptions(disease.getUuid())).withRel("prescriptions"));
        model.add(linkTo(methodOn(DiseaseController.class).getDisease(disease.getUuid())).withSelfRel());
        return model;
    }
}
