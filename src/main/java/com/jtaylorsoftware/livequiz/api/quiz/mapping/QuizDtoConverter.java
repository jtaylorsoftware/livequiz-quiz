package com.jtaylorsoftware.livequiz.api.quiz.mapping;

import com.jtaylorsoftware.livequiz.api.quiz.model.Quiz;
import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class QuizDtoConverter implements DtoToModelConverter<QuizDto, Quiz> {
    private final QuestionDtoConverter questionDtoConverter;

    @Override
    public Quiz toModel(QuizDto quizDto) {
        val questions = quizDto.getQuestions().stream().map(questionDtoConverter::toModel).toList();
        return Quiz.builder()
            .title(quizDto.getTitle())
            .questions(questions)
            .build();
    }
}
