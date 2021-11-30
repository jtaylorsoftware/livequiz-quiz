package com.jtaylorsoftware.livequiz.api.quiz.dao;

import com.jtaylorsoftware.livequiz.api.quiz.model.Quiz;

/**
 * Provides Quiz persistence.
 */
public interface QuizDao {
    /**
     * Determines if a Quiz exists, using its id.
     * @param id Id of Quiz to check.
     * @throws DatabaseException If an internal database error occurs.
     * @return {@code true} if it exists, {@code false} otherwise.
     */
    Boolean existsById(String id);

    /**
     * Retrieves a Quiz from the database using properties of the supplied Quiz.
     * @param quiz Quiz to use for search.
     * @throws DatabaseException If an internal database error occurs.
     * @return The found Quiz.
     */
    Quiz find(Quiz quiz);

    /**
     * Persists or updates a Quiz. If given an existing Quiz, will perform a replacement, potentially
     * losing previously saved properties. Sets the dateCreated and lastUpdated timestamps on the Quiz
     * to the current time.
     * @throws DatabaseException If an internal database error occurs.
     * @param quiz Quiz model to save.
     * @return The created Quiz.
     */
    Quiz create(Quiz quiz);

    /**
     * Updates an existing Quiz using data from the argument model. Argument model can contain nulls,
     * in which case those properties will skipped in the update. Sets the lastUpdated timestamp on the
     * Quiz to the current time.
     * @param quiz Partial or full Quiz data to use in the update.
     * @throws DatabaseException If an internal database error occurs.
     * @return The updated Quiz.
     */
    Quiz save(Quiz quiz);

    /**
     * Deletes an existing Quiz using the id of argument to perform deletion.
     * @throws DatabaseException If an internal database error occurs.
     * @param quiz Quiz
     */
    void delete(Quiz quiz);
}

