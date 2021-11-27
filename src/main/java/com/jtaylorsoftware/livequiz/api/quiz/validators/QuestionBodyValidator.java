package com.jtaylorsoftware.livequiz.api.quiz.validators;

import com.jtaylorsoftware.livequiz.api.quiz.mapping.QuestionDto;
import com.jtaylorsoftware.livequiz.api.quiz.model.Question;
import lombok.val;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class QuestionBodyValidator implements ConstraintValidator<ValidQuestionBody, QuestionDto.QuestionBody> {
    @Override
    public boolean isValid(QuestionDto.QuestionBody questionBody, ConstraintValidatorContext context) {
        val requiredTypes = "MULTIPLE_CHOICE|FILL_IN";
        val pattern = Pattern.compile(requiredTypes);
        if (questionBody.getType() == null) {
            return false;
        }
        val matcher = pattern.matcher(questionBody.getType());
        if (!matcher.matches()) {
            context.buildConstraintViolationWithTemplate("Type must be one of " + requiredTypes)
                .addPropertyNode("type")
                .addConstraintViolation();
            return false;
        }
        val type = Question.QuestionType.valueOf(matcher.group());
        if (type == Question.QuestionType.MULTIPLE_CHOICE) {
            val mcBody = (QuestionDto.MultipleChoiceQuestionBody) questionBody;
            if (mcBody.getAnswerIndex() >= mcBody.getChoices().size()) {
                context.buildConstraintViolationWithTemplate("answerIndex must be less than number of choices")
                    .addPropertyNode("anserIndex")
                    .addConstraintViolation();
                return false;
            }
        }
        return true;
    }
}
