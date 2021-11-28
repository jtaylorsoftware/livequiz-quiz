package com.jtaylorsoftware.livequiz.api.quiz.mapping;

import com.jtaylorsoftware.livequiz.api.quiz.model.Question;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class QuestionDtoConverterTests {
    @Test
    void multipleChoice_convertsCorrectly() {
        val bodyDto = QuestionDto.MultipleChoiceQuestionBody.builder()
            .answerIndex(0)
            .choices(List.of(new QuestionDto.MultipleChoiceQuestionBody.Choice("Choice")))
            .build();
        val questionDto = QuestionDto.builder()
            .prompt("Question")
            .imageUrl("Image")
            .body(bodyDto)
            .build();
        val converted = new QuestionDtoConverter().toModel(questionDto);
        assertThat(converted.getPrompt(), is(equalTo(questionDto.getPrompt())));
        assertThat(converted.getImageUrl(), is(equalTo(questionDto.getImageUrl())));
        assertThat(converted.getBody().getType(), is(equalTo(Question.QuestionType.MULTIPLE_CHOICE)));
        assertThat(converted.getBody(), is(instanceOf(Question.MultipleChoiceQuestionBody.class)));
        val convertedBody = (Question.MultipleChoiceQuestionBody) converted.getBody();
        assertThat(convertedBody.getAnswerIndex(), is(equalTo(bodyDto.getAnswerIndex())));
        assertThat(convertedBody.getChoices().size(), is(equalTo(bodyDto.getChoices().size())));
        for (int i = 0; i < convertedBody.getChoices().size(); i++) {
            assertThat(convertedBody.getChoices().get(i).getText(), is(equalTo(bodyDto.getChoices().get(i).getText())));
        }
    }

    @Test
    void fillIn_convertsCorrectly() {
        val bodyDto = QuestionDto.FillInQuestionBody.builder()
            .answer("Answer")
            .build();
        val questionDto = QuestionDto.builder()
            .prompt("Question")
            .imageUrl("Image")
            .body(bodyDto)
            .build();
        val converted = new QuestionDtoConverter().toModel(questionDto);
        assertThat(converted.getPrompt(), is(equalTo(questionDto.getPrompt())));
        assertThat(converted.getImageUrl(), is(equalTo(questionDto.getImageUrl())));
        assertThat(converted.getBody().getType(), is(equalTo(Question.QuestionType.FILL_IN)));
        assertThat(converted.getBody(), is(instanceOf(Question.FillInQuestionBody.class)));
        val convertedBody = (Question.FillInQuestionBody) converted.getBody();
        assertThat(convertedBody.getAnswer(), is(equalTo(bodyDto.getAnswer())));
    }
}
