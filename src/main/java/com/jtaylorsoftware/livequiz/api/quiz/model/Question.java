package com.jtaylorsoftware.livequiz.api.quiz.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonView;
import com.jtaylorsoftware.livequiz.api.quiz.mapping.Views;
import lombok.*;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbConvertedBy;

import java.util.List;

@DynamoDbBean
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class Question {
    private String prompt;
    private String imageUrl;
    private QuestionBody body;

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @DynamoDbConvertedBy(QuestionBodyConverter.class)
    public QuestionBody getBody() {
        return body;
    }

    public void setBody(QuestionBody body) {
        this.body = body;
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type")
    @JsonSubTypes({
        @JsonSubTypes.Type(value = MultipleChoiceQuestionBody.class, name = "MULTIPLE_CHOICE"),
        @JsonSubTypes.Type(value = FillInQuestionBody.class, name = "FILL_IN"),
    })
    public interface QuestionBody {
        QuestionType getType();
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @ToString
    @EqualsAndHashCode
    public static class MultipleChoiceQuestionBody implements QuestionBody {
        private final static QuestionType type = QuestionType.MULTIPLE_CHOICE;

        private List<Choice> choices;

        @JsonView(Views.Internal.class)
        private Integer answerIndex;

        @Override
        public QuestionType getType() {
            return type;
        }

        @DynamoDbBean
        @Data
        public static class Choice {
            private String text;
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @ToString
    @EqualsAndHashCode
    public static class FillInQuestionBody implements QuestionBody {
        private final static QuestionType type = QuestionType.FILL_IN;

        @JsonView(Views.Internal.class)
        private String answer;

        @Override
        public QuestionType getType() {
            return type;
        }
    }

    public enum QuestionType {
        MULTIPLE_CHOICE, FILL_IN
    }
}


