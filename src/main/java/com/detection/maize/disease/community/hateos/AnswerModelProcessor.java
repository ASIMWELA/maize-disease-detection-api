package com.detection.maize.disease.community.hateos;

import com.detection.maize.disease.community.CommunityController;
import com.detection.maize.disease.community.entity.AnswerEntity;
import com.detection.maize.disease.community.repository.AnswerRepository;
import com.detection.maize.disease.exception.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * @author Augustine Simwela
 *
 * A processor of answer entities
 *
 * All answer Models passes here before being returned to the user
 *
 * Transforms the models to include links for all those models with images
 */
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AnswerModelProcessor implements RepresentationModelProcessor<AnswerModel> {

    /**
     * A DAO layer for accessing this answer
     */
    AnswerRepository answerRepository;

    /**
     * Constructs an object of this class
     *
     * @param answerRepository of {@link AnswerRepository}
     */
    public AnswerModelProcessor(AnswerRepository answerRepository) {
        this.answerRepository = answerRepository;
    }


    /**
     * Tranforms an answer model to include a link of its image if it contains an image
     *
     * @param model of {@link AnswerModel}
     *
     * @return AnswerModel
     *
     * @see AnswerModel
     *
     * @see AnswerEntity
     */
    @Override
    @Transactional
    public AnswerModel process(AnswerModel model) {
        AnswerEntity answerEntity = answerRepository.findByUuid(model.getUuid()).orElseThrow(
                ()-> new EntityNotFoundException("No answer with the given identity")
        );

        if(answerEntity.getAnswerImage() != null){
            model.add(linkTo(methodOn(CommunityController.class).getAnswerImage(answerEntity.getUuid())).withRel("answerImage"));
        }
        return model;
    }
}
