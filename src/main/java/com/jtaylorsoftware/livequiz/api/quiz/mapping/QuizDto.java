package com.jtaylorsoftware.livequiz.api.quiz.mapping;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class QuizDto {
    @NotBlank(message = "Title cannot be empty.")
    private String title;

    @Valid
    @NotNull(message = "Questions must be present")
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

