package com.jtaylorsoftware.livequiz.api.quiz.exception;

import lombok.Getter;
import lombok.Value;

import java.util.ArrayList;
import java.util.List;

/**
 * Readable response body for exceptions that includes both the failing value and
 * a reason why it failed.
 */
@Value
public class ErrorResponse {
    @Getter
    List<ErrorInfo> errors = new ArrayList<>();

    public record ErrorInfo(String field, String value, String reason) { }
}