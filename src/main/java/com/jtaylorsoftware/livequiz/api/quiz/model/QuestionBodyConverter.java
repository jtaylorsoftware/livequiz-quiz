package com.jtaylorsoftware.livequiz.api.quiz.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import software.amazon.awssdk.enhanced.dynamodb.AttributeConverter;
import software.amazon.awssdk.enhanced.dynamodb.AttributeValueType;
import software.amazon.awssdk.enhanced.dynamodb.EnhancedType;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

public class QuestionBodyConverter implements AttributeConverter<Question.QuestionBody> {
    private static final ObjectMapper mapper = new ObjectMapper();

    @SneakyThrows
    @Override
    public AttributeValue transformFrom(Question.QuestionBody input) {
        return AttributeValue.builder().s(mapper.writeValueAsString(input)).build();
    }

    @SneakyThrows
    @Override
    public Question.QuestionBody transformTo(AttributeValue input) {
        return mapper.readValue(input.s(), Question.QuestionBody.class);
    }

    @Override
    public EnhancedType<Question.QuestionBody> type() {
        return EnhancedType.of(Question.QuestionBody.class);
    }

    @Override
    public AttributeValueType attributeValueType() {
        return AttributeValueType.S;
    }
}
