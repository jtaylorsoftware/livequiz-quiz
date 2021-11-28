package com.jtaylorsoftware.livequiz.api.quiz.service;

import com.jtaylorsoftware.livequiz.api.quiz.dao.DatabaseException;
import com.jtaylorsoftware.livequiz.api.quiz.dao.QuizDao;
import com.jtaylorsoftware.livequiz.api.quiz.exception.QuizNotFoundException;
import com.jtaylorsoftware.livequiz.api.quiz.exception.ServiceException;
import com.jtaylorsoftware.livequiz.api.quiz.mapping.QuestionDtoConverter;
import com.jtaylorsoftware.livequiz.api.quiz.mapping.QuizDto;
import com.jtaylorsoftware.livequiz.api.quiz.mapping.QuizDtoConverter;
import com.jtaylorsoftware.livequiz.api.quiz.model.Quiz;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuizServiceTests {
    @Mock
    QuizDao quizDao;
    @Mock
    QuizDtoConverter converter;

    private QuizService quizService;

    @BeforeEach
    void setup() {
        quizService = new QuizServiceImpl(quizDao, converter);
    }

    @Test
    void findById_whenDaoThrows_throwsServiceException() {
        when(quizDao.find(any())).thenThrow(DatabaseException.class);
        assertThrows(ServiceException.class, () -> quizService.findById(""));
    }

    @Test
    void findById_returnsDaoResult() {
        val id = "TEST_ID";
        val testQuiz = Quiz.builder().id(id).build();
        when(quizDao.find(testQuiz)).thenReturn(testQuiz);
        val found = quizService.findById(id);
        assertThat(found.isPresent(), is(true));
        assertThat(found.get(), is(equalTo(testQuiz)));
    }

    @Test
    void create_whenDaoThrows_throwsServiceException() {
        doThrow(DatabaseException.class).when(quizDao).create(any());
        val quiz = QuizDto.builder().build();
        when(converter.toModel(quiz)).thenReturn(Quiz.builder().build());
        assertThrows(ServiceException.class, () -> quizService.create(quiz, "TEST"));
    }

    @Test
    void create_setsCreatedBy() {
        val quiz = QuizDto.builder().build();
        val id = "TEST";
        val createdBy = "TEST";
        when(converter.toModel(quiz)).thenReturn(Quiz.builder().id(id).build());
        val newQuiz = quizService.create(quiz, createdBy);
        assertThat(newQuiz.getCreatedBy(), is(equalTo(createdBy)));
        verify(quizDao, times(1)).create(any());
    }

    @Test
    void save_whenDaoThrows_throwsServiceException() {
        doThrow(DatabaseException.class).when(quizDao).existsById(any());
        val quiz = QuizDto.builder().build();
        assertThrows(ServiceException.class, () -> quizService.save("", quiz));
    }

    @Test
    void save_whenQuizNotExists_throwsQuizNotFoundException() {
        when(quizDao.existsById(any())).thenReturn(false);
        val quiz = QuizDto.builder().build();
        val exception = assertThrows(QuizNotFoundException.class, () -> quizService.save("TEST", quiz));
        assertThat(exception.getId(), is(equalTo("TEST")));
    }

    @Test
    void save_whenQuizExists_savesUpdates() {
        val id = "TEST";
        val newTitle = "NEW_TITLE";
        val updated = QuizDto.builder().title(newTitle).build();
        when(quizDao.existsById(any())).thenReturn(true);
        when(quizDao.save(any())).then(returnsFirstArg());
        when(converter.toModel(updated)).thenReturn(Quiz.builder().title(newTitle).build());
        val saved = quizService.save(id, updated);
        verify(quizDao, times(1)).save(any());
        assertThat(saved.getTitle(), is(equalTo(newTitle)));
    }

    @Test
    void delete_whenDaoThrows_throwsServiceException() {
        doThrow(DatabaseException.class).when(quizDao).existsById(any());
        assertThrows(ServiceException.class, () -> quizService.delete(""));
    }

    @Test
    void delete_whenQuizNotExists_throwsQuizNotFoundException() {
        when(quizDao.existsById(any())).thenReturn(false);
        val exception = assertThrows(QuizNotFoundException.class, () -> quizService.delete("TEST"));
        assertThat(exception.getId(), is(equalTo("TEST")));
    }

    @Test
    void delete_whenQuizExists_deletes(){
        when(quizDao.existsById(any())).thenReturn(true);
        val argCapture = ArgumentCaptor.forClass(Quiz.class);
        val id = "TEST";
        quizService.delete(id);
        verify(quizDao).delete(argCapture.capture());
        val captured = argCapture.getValue();
        assertThat(captured.getId(), is(equalTo(id)));
    }
}
