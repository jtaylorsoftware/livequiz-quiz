package com.jtaylorsoftware.livequiz.api.quiz.service;

import com.jtaylorsoftware.livequiz.api.quiz.dao.DatabaseException;
import com.jtaylorsoftware.livequiz.api.quiz.dao.QuizDao;
import com.jtaylorsoftware.livequiz.api.quiz.exception.QuizNotFoundException;
import com.jtaylorsoftware.livequiz.api.quiz.exception.ServiceException;
import com.jtaylorsoftware.livequiz.api.quiz.mapping.QuizDto;
import com.jtaylorsoftware.livequiz.api.quiz.mapping.QuizDtoConverter;
import com.jtaylorsoftware.livequiz.api.quiz.model.Quiz;
import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class QuizServiceImpl implements QuizService {
    private final QuizDao quizDao;
    private final QuizDtoConverter quizDtoConverter;

    @Override
    public Optional<Quiz> findById(String id) {
        try {
            return Optional.ofNullable(quizDao.find(Quiz.builder().id(id).build()));
        } catch (DatabaseException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Quiz save(String id, QuizDto quizDto) {
        try {
            if (!quizDao.existsById(id)) {
                throw new QuizNotFoundException(id);
            }
            val updatedQuiz = quizDtoConverter.toModel(quizDto);
            updatedQuiz.setId(id);
            return quizDao.save(updatedQuiz);
        } catch (DatabaseException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Quiz create(QuizDto quizDto, String createdBy) {
        try {
            val newQuiz = quizDtoConverter.toModel(quizDto);
            newQuiz.setCreatedBy(createdBy);
            return quizDao.create(newQuiz);
        } catch (DatabaseException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void delete(String id) {
        try {
            if (!quizDao.existsById(id)) {
                throw new QuizNotFoundException(id);
            }
            quizDao.delete(Quiz.builder().id(id).build());
        } catch (DatabaseException e) {
            throw new ServiceException(e);
        }
    }
}
