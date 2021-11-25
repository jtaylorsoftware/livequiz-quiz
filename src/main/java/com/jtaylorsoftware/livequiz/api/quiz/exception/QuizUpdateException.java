package com.jtaylorsoftware.livequiz.api.quiz.exception;

public class QuizUpdateException extends RuntimeException {
    public QuizUpdateException() {
        super("Could not save Quiz");
    }
}
