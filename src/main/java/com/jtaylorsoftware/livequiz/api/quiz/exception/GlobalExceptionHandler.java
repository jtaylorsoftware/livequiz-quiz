package com.jtaylorsoftware.livequiz.api.quiz.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

import static java.util.Map.entry;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(QuizNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Object handleQuizNotFound(QuizNotFoundException e){
        return Map.ofEntries(entry("reason", e.getMessage()));
    }

    @ExceptionHandler(QuizCreationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Object handleQuizCreationException(QuizCreationException e){
        return Map.ofEntries(entry("reason", e.getMessage()));
    }


    @ExceptionHandler(QuizUpdateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Object handleQuizUpdateException(QuizUpdateException e){
        return Map.ofEntries(entry("reason", e.getMessage()));
    }
}
