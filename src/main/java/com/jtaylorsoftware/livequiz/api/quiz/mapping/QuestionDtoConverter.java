package com.jtaylorsoftware.livequiz.api.quiz.mapping;

import com.jtaylorsoftware.livequiz.api.quiz.model.Question;
import lombok.val;
import org.springframework.stereotype.Component;

@Component
public class QuestionDtoConverter implements DtoToModelConverter<QuestionDto, Question> {
    @Override
    public Question toModel(QuestionDto questionDto) {
        val questionType = Question.QuestionType.valueOf(questionDto.getBody().getType());
        switch (questionType) {
            case MULTIPLE_CHOICE -> {
                val mcBody = (QuestionDto.MultipleChoiceQuestionBody) questionDto.getBody();
                val choices = mcBody.getChoices()
                    .stream()
                    .map(choice -> new Question.MultipleChoiceQuestionBody.Choice(choice.getText()))
                    .toList();
                val body = Question.MultipleChoiceQuestionBody.builder()
                    .choices(choices)
                    .answerIndex(mcBody.getAnswerIndex())
                    .type(Question.QuestionType.MULTIPLE_CHOICE)
                    .build();
                return Question.builder()
                    .prompt(questionDto.getPrompt())
                    .imageUrl(questionDto.getImageUrl())
                    .body(body)
                    .build();
            }
            case FILL_IN -> {
                val fillInBody = (QuestionDto.FillInQuestionBody) questionDto.getBody();
                val body = Question.FillInQuestionBody.builder()
                    .answer(fillInBody.getAnswer())
                    .type(Question.QuestionType.FILL_IN)
                    .build();
                return Question.builder()
                    .prompt(questionDto.getPrompt())
                    .imageUrl(questionDto.getImageUrl())
                    .body(body)
                    .build();
            }
            default -> throw new IllegalArgumentException("Invalid Question body type");
        }
    }
}
