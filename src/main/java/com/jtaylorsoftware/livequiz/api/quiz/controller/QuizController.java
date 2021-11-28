package com.jtaylorsoftware.livequiz.api.quiz.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.jtaylorsoftware.livequiz.api.quiz.service.QuizService;
import com.jtaylorsoftware.livequiz.api.quiz.controller.assembler.QuizModelAssembler;
import com.jtaylorsoftware.livequiz.api.quiz.exception.QuizNotFoundException;
import com.jtaylorsoftware.livequiz.api.quiz.mapping.QuizDto;
import com.jtaylorsoftware.livequiz.api.quiz.mapping.Views;
import com.jtaylorsoftware.livequiz.api.quiz.model.Quiz;
import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@AllArgsConstructor
@RequestMapping("/api/quizzes")
public class QuizController {
    private final QuizService quizService;
    private final QuizModelAssembler assembler;

    @PostMapping
    @JsonView(Views.Internal.class)
    public ResponseEntity<?> create(@Valid @RequestBody QuizDto quiz) {
        val newQuiz = quizService.create(quiz, "");
        return ResponseEntity.created(linkTo(methodOn(QuizController.class).getOne(newQuiz.getId())).toUri()).body(
            assembler.toModel(newQuiz)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        quizService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    @JsonView(Views.Internal.class)
    public ResponseEntity<?> update(@PathVariable String id, @Valid @RequestBody QuizDto quiz) {
        val updatedQuiz = quizService.save(id, quiz);
        return ResponseEntity.ok(assembler.toModel(updatedQuiz));
    }

    @GetMapping("/{id}")
    @JsonView(Views.Public.class)
    public EntityModel<Quiz> getOne(@PathVariable String id) {
        val quiz = quizService.findById(id).orElseThrow(() -> new QuizNotFoundException(id));
        return assembler.toModel(quiz);
    }
}
