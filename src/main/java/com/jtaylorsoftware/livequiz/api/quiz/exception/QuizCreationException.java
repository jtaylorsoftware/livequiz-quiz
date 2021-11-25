package com.jtaylorsoftware.livequiz.api.quiz.exception;

public class QuizCreationException extends RuntimeException {
    public QuizCreationException() {
        super("Could not create Quiz");
    }
}
