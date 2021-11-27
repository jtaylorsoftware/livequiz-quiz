package com.jtaylorsoftware.livequiz.api.quiz.dao;

/**
 * Generic data access layer exception.
 */
public class DatabaseException extends RuntimeException {
    private static final String reason = "Could not handle operation right now.";
    public DatabaseException() {
        super(reason);
    }

    public DatabaseException(Throwable cause) {
        super(reason, cause);
    }
}