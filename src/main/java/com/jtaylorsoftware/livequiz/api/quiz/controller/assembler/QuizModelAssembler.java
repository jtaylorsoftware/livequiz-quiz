package com.jtaylorsoftware.livequiz.api.quiz.controller.assembler;

import com.jtaylorsoftware.livequiz.api.quiz.controller.QuizController;
import com.jtaylorsoftware.livequiz.api.quiz.model.Quiz;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class QuizModelAssembler implements RepresentationModelAssembler<Quiz, EntityModel<Quiz>> {

    @Override
    public EntityModel<Quiz> toModel(Quiz quiz) {
        return EntityModel.of(
            quiz,
            // Link to self
            linkTo(methodOn(QuizController.class).getOne(quiz.getId())).withSelfRel()
        );
    }
}
