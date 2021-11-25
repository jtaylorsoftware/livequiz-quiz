package com.jtaylorsoftware.livequiz.api.quiz.Service;

import com.jtaylorsoftware.livequiz.api.quiz.mapping.QuizDto;
import com.jtaylorsoftware.livequiz.api.quiz.model.Quiz;
import com.jtaylorsoftware.livequiz.api.quiz.dao.QuizDao;
import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class QuizServiceImpl implements QuizService {
    private final QuizDao quizDao;

    @Override
    public Optional<Quiz> findById(String id) {
        return Optional.ofNullable(quizDao.find(Quiz.builder().id(id).build()));
    }

    @Override
    public Quiz save(QuizDto quiz) {
        val updatedQuiz = Quiz.builder()
            .createdBy(null)
            .dateCreated(null)
            .title(quiz.getTitle())
            .questions(quiz.getQuestions())
            .build();
        return quizDao.save(updatedQuiz);
    }

    @Override
    public Quiz create(QuizDto quiz) {
        val newQuiz = Quiz.builder()
            .createdBy(null)
            .dateCreated(null)
            .title(quiz.getTitle())
            .questions(quiz.getQuestions())
            .build();
        quizDao.create(newQuiz);
        return newQuiz;
    }

    @Override
    public void delete(Quiz quiz) {
        quizDao.delete(quiz);
    }

    @Override
    public Boolean quizExists(String id) {
        return quizDao.existsById(id);
    }
}
