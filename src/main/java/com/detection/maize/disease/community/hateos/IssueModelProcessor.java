package com.detection.maize.disease.community.hateos;

import com.detection.maize.disease.commons.Constants;
import com.detection.maize.disease.community.CommunityController;
import com.detection.maize.disease.community.entity.IssueEntity;
import com.detection.maize.disease.community.repository.IssueRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class IssueModelProcessor implements RepresentationModelProcessor<IssueModel> {
    IssueRepository issueRepository;
    public IssueModelProcessor(IssueRepository issueRepository) {
        this.issueRepository = issueRepository;
    }

    @Override
    @Transactional
    public IssueModel process(IssueModel model) {
        //add image url link only if the image is present
        IssueEntity issueEntity = issueRepository.findByUuid(model.getUuid()).orElseThrow(
                ()->new EntityNotFoundException("No issue with the id provided")
        );
        if(issueEntity.getIssueImage() != null){
            model.add(linkTo(methodOn(CommunityController.class).getIssueImageUrl(model.getUuid())).withRel("imageUrl"));
        }
        if(issueEntity.getAnswers() != null){
            model.add(linkTo(methodOn(CommunityController.class).getAnswers(Constants.PAGE, Constants.SIZE, null, model.getUuid())).withRel("issueAnswers"));
        }
        return model;
    }
}
