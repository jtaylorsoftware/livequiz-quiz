package com.jtaylorsoftware.livequiz.api.quiz.exception;

import lombok.Getter;

public class QuizNotFoundException extends RuntimeException {
    @Getter private final String id;

    public QuizNotFoundException(String id) {
        super("Could not find Quiz with id=" + id);
        this.id = id;
    }
}
