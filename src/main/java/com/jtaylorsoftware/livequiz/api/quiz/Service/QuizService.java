package com.jtaylorsoftware.livequiz.api.quiz.Service;

import com.jtaylorsoftware.livequiz.api.quiz.exception.QuizNotFoundException;
import com.jtaylorsoftware.livequiz.api.quiz.exception.ServiceException;
import com.jtaylorsoftware.livequiz.api.quiz.mapping.QuizDto;
import com.jtaylorsoftware.livequiz.api.quiz.model.Quiz;

import java.util.Optional;

/**
 * Provides convenience methods for creating, finding, or manipulating quizzes.
 */
public interface QuizService {
    /**
     * Looks up a Quiz by its id.
     * @param id Id of the Quiz to find.
     * @return An {@code Optional} with the Quiz if found, or empty if not found.
     */
    Optional<Quiz> findById(String id);

    /**
     * Updates an existing Quiz with new data. Some fields (such as creation date) are immutable
     * and can never be changed.
     * @param id Id of Quiz to update.
     * @param quizDto Data to replace in the existing Quiz. Nulls in the argument's properties can be used to
     *                skip updating for that property.
     * @throws ServiceException If there's an exception updating.
     * @throws QuizNotFoundException If the Quiz does not already exist.
     * @return The newly saved Quiz.
     */
    Quiz save(String id, QuizDto quizDto);

    /**
     * Creates a new Quiz. Some fields (such as creation date) cannot be set by the client and instead
     * must be set by the implementation.
     * @param quizDto Quiz to create.
     * @param createdBy User creating the Quiz.
     * @throws ServiceException If there's an exception creating.
     * @return The newly created Quiz.
     */
    Quiz create(QuizDto quizDto, String createdBy);

    /**
     * Deletes a Quiz by using its id.
     * @param id Id of Quiz to delete.
     * @throws QuizNotFoundException If the Quiz did not previously exist.
     * @throws ServiceException If there's some other exception deleting the Quiz.
     */
    void delete(String id);
}
