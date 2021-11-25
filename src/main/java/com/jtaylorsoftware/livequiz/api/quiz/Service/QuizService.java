package com.jtaylorsoftware.livequiz.api.quiz.Service;

import com.jtaylorsoftware.livequiz.api.quiz.exception.QuizCreationException;
import com.jtaylorsoftware.livequiz.api.quiz.exception.QuizUpdateException;
import com.jtaylorsoftware.livequiz.api.quiz.mapping.QuizDto;
import com.jtaylorsoftware.livequiz.api.quiz.model.Quiz;

import java.util.Optional;

public interface QuizService {
    Boolean quizExists(String id);

    Optional<Quiz> findById(String id);

    /**
     *
     * @throws QuizUpdateException
     */
    Quiz save(QuizDto quiz);

    /**
     *
     * @throws QuizCreationException
     */
    Quiz create(QuizDto quiz);

    void delete(Quiz quiz);
}
