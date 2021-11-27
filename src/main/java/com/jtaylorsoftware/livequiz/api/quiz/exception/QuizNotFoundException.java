package com.jtaylorsoftware.livequiz.api.quiz.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Service layer exception that occurs when a Quiz resource can't be located.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class QuizNotFoundException extends ServiceException {
    @Getter private final String id;

    public QuizNotFoundException(String id) {
        super("Could not find Quiz with id=" + id);
        this.id = id;
    }
}
