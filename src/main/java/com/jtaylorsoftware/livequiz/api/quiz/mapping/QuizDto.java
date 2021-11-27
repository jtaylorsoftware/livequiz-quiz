package com.jtaylorsoftware.livequiz.api.quiz.mapping;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@Builder
public class QuizDto {
    @NotBlank(message = "Title cannot be empty.")
    @NonNull
    private String title;

    @NotNull(message = "Questions must be present")
    @NonNull
    @Size(min = 1, max = 20, message = "Must have between 1 and 20 questions")
    private List<QuestionDto> questions;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<QuestionDto> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionDto> questions) {
        this.questions = questions;
    }
}

