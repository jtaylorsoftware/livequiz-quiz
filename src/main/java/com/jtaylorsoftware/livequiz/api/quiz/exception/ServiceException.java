package com.jtaylorsoftware.livequiz.api.quiz.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Generic service layer exception.
 */
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ServiceException extends RuntimeException {
    private static final String reason = "Could not handle operation right now.";

    public ServiceException() {
        super(reason);
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(Throwable cause) {
        super(reason, cause);
    }
}