package com.jtaylorsoftware.livequiz.api.quiz.mapping;

import com.jtaylorsoftware.livequiz.api.quiz.model.Question;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class QuizDtoConverterTests {
    @Test
    void convertsCorrectly(@Mock QuestionDtoConverter questionDtoConverter) {
        val bodyDto = QuestionDto.FillInQuestionBody.builder()
            .answer("Answer")
            .build();
        val questionDto = QuestionDto.builder()
            .prompt("Prompt")
            .imageUrl("Image")
            .body(bodyDto)
            .build();
        val question = Question.builder()
            .prompt("Prompt")
            .imageUrl("Image")
            .body(Question.FillInQuestionBody.builder().answer("Answer").build())
            .build();
        val quizDto = QuizDto.builder()
            .title("Title")
            .questions(List.of(questionDto))
            .build();

        val converter = new QuizDtoConverter(questionDtoConverter);
        when(questionDtoConverter.toModel(questionDto)).thenReturn(question);
        val converted = converter.toModel(quizDto);
        assertThat(converted.getTitle(), is(equalTo(quizDto.getTitle())));
        assertThat(converted.getQuestions(), hasSize(quizDto.getQuestions().size()));
        assertThat(converted.getQuestions(), contains(question));
    }
}
