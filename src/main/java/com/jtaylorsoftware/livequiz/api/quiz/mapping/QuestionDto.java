package com.jtaylorsoftware.livequiz.api.quiz.mapping;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.jtaylorsoftware.livequiz.api.quiz.model.Question;
import com.jtaylorsoftware.livequiz.api.quiz.validators.ValidQuestionBody;
import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@Builder
public class QuestionDto {
    @NotBlank(message = "Question must not be blank")
    private String prompt;

    private String imageUrl;

    @ValidQuestionBody
    @NotNull(message = "Question body must not be null")
    private QuestionBody body;

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type")
    @JsonSubTypes({
        @JsonSubTypes.Type(value = Question.MultipleChoiceQuestionBody.class, name = "MULTIPLE_CHOICE"),
        @JsonSubTypes.Type(value = Question.FillInQuestionBody.class, name = "FILL_IN"),
    })
    public interface QuestionBody {
        String getType();
    }

    @Data
    @Builder
    public static class MultipleChoiceQuestionBody implements QuestionDto.QuestionBody {
        @NotBlank(message = "Type must not be blank")
        @Builder.Default
        private String type = Question.QuestionType.MULTIPLE_CHOICE.name();

        @NotNull
        @Size(min = 1, max = 4, message = "Must have between 1 and 4 choices")
        private List<Choice> choices;

        @NotNull
        @Min(value = 0, message = "Must have non-negative answerIndex")
        private Integer answerIndex;

        @Override
        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public List<Choice> getChoices() {
            return choices;
        }

        public void setChoices(List<Choice> choices) {
            this.choices = choices;
        }

        public Integer getAnswerIndex() {
            return answerIndex;
        }

        public void setAnswerIndex(Integer answerIndex) {
            this.answerIndex = answerIndex;
        }

        @Data
        @Builder
        public static class Choice {
            @NotBlank(message = "Question choice must not be blank")
            private String text;
        }
    }

    @Data
    @Builder
    public static class FillInQuestionBody implements QuestionDto.QuestionBody {
        @NotBlank(message = "Type must not be blank")
        @Builder.Default
        private String type = Question.QuestionType.FILL_IN.name();

        @NotBlank(message = "Answer must not be blank")
        private String answer;

        @Override
        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getAnswer() {
            return answer;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }
    }
}
