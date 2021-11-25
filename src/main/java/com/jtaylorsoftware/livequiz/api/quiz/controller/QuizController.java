package com.jtaylorsoftware.livequiz.api.quiz.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.jtaylorsoftware.livequiz.api.quiz.Service.QuizService;
import com.jtaylorsoftware.livequiz.api.quiz.exception.QuizNotFoundException;
import com.jtaylorsoftware.livequiz.api.quiz.mapping.QuizDto;
import com.jtaylorsoftware.livequiz.api.quiz.mapping.Views;
import com.jtaylorsoftware.livequiz.api.quiz.model.Quiz;
import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@AllArgsConstructor
@RequestMapping("/api/quizzes")
public class QuizController {
    private final QuizService quizService;

    @PostMapping
    @JsonView(Views.Internal.class)
    public ResponseEntity<?> create(@RequestBody QuizDto quiz) {
        val newQuiz = quizService.create(quiz);
        return ResponseEntity.created(linkTo(methodOn(QuizController.class).getCreatedOne(newQuiz.getId())).toUri()).body(
            EntityModel.of(newQuiz, linkTo(methodOn(QuizController.class).getOne(newQuiz.getId())).withSelfRel())
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody QuizDto quiz) {
        if (!quizService.quizExists(id)){
            throw new QuizNotFoundException(id);
        }
        quizService.save(quiz);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @JsonView(Views.Public.class)
    public EntityModel<Quiz> getOne(@PathVariable String id) {
        val quiz = quizService.findById(id).orElseThrow(() -> new QuizNotFoundException(id));
        return EntityModel.of(quiz, linkTo(methodOn(QuizController.class).getOne(id)).withSelfRel());
    }

    @GetMapping("/created/{id}")
    @JsonView(Views.Internal.class)
    public EntityModel<Quiz> getCreatedOne(@PathVariable String id) {
        val quiz = quizService.findById(id).orElseThrow(() -> new QuizNotFoundException(id));
        return EntityModel.of(quiz, linkTo(methodOn(QuizController.class).getCreatedOne(id)).withSelfRel());
    }
}
