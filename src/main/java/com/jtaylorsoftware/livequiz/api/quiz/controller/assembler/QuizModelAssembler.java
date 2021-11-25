package com.jtaylorsoftware.livequiz.api.quiz.controller.assembler;

import com.jtaylorsoftware.livequiz.api.quiz.model.Quiz;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class QuizModelAssembler implements RepresentationModelAssembler<Quiz, EntityModel<Quiz>> {

    @Override
    public EntityModel<Quiz> toModel(Quiz entity) {
        return null;
    }
}
