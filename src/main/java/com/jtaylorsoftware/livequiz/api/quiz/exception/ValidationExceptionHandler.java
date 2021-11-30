package com.jtaylorsoftware.livequiz.api.quiz.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * {@code RestControllerAdvice} for handling Json/RequestBody validation exceptions.
 * Maps exceptions to a response in the format:
 * <pre>
 * {@code
 *     "errors": [
 *       {
 *         "field":"Some field",
 *         "value":"Some value",
 *         "reason":"Some reason"
 *       }
 *     ]
 * }
 * </pre>
 */
@RestControllerAdvice
@RequiredArgsConstructor
public class ValidationExceptionHandler {
    private final ObjectMapper objectMapper;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ErrorResponse onMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        val errorResponse = new ErrorResponse();
        for (val fieldError : e.getBindingResult().getFieldErrors()) {
            String errorValue;
            try {
                errorValue = objectMapper.writeValueAsString(fieldError.getRejectedValue());
            } catch (JsonProcessingException jsonProcessingException) {
                errorValue = "";
            }
            errorResponse.getErrors().add(new ErrorResponse.ErrorInfo(
                fieldError.getField(),
                errorValue,
                fieldError.getDefaultMessage()));
        }
        return errorResponse;
    }
}