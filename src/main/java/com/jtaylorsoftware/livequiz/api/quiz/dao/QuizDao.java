package com.jtaylorsoftware.livequiz.api.quiz.dao;

import com.jtaylorsoftware.livequiz.api.quiz.model.Quiz;

public interface QuizDao {
    Boolean existsById(String id);
    Quiz find(Quiz quiz);
    Quiz create(Quiz quiz);
    Quiz save(Quiz quiz);
    void delete(Quiz quiz);
}

