package com.jtaylorsoftware.livequiz.api.quiz.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jtaylorsoftware.livequiz.api.quiz.JacksonTestConfig;
import com.jtaylorsoftware.livequiz.api.quiz.controller.assembler.QuizModelAssembler;
import com.jtaylorsoftware.livequiz.api.quiz.mapping.QuestionDto;
import com.jtaylorsoftware.livequiz.api.quiz.mapping.QuizDto;
import com.jtaylorsoftware.livequiz.api.quiz.model.Question;
import com.jtaylorsoftware.livequiz.api.quiz.model.Quiz;
import com.jtaylorsoftware.livequiz.api.quiz.service.QuizService;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Map.entry;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(QuizController.class)
@Import(JacksonTestConfig.class)
class QuizControllerMvcTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private QuizService quizService;

    @MockBean
    private QuizModelAssembler modelAssembler;

    @Autowired
    private ObjectMapper objectMapper;

    // Data being sent as requests
    private QuestionDto questionDto;
    private QuizDto quizDto;
    // Responses matching DTO requests
    private Question question;
    private Quiz quiz;

    @BeforeEach
    void setup() {
        // Create some Quizzes for testing
        questionDto = QuestionDto.builder()
            .prompt("TEST")
            .imageUrl("TEST")
            .body(
                QuestionDto.MultipleChoiceQuestionBody.builder()
                    .answerIndex(0)
                    .choices(List.of(QuestionDto.MultipleChoiceQuestionBody.Choice.builder().text("TEST").build()))
                    .build())
            .build();
        quizDto = QuizDto.builder()
            .title("TEST")
            .questions(List.of(questionDto))
            .build();
        question = Question.builder()
            .prompt(questionDto.getPrompt())
            .imageUrl(questionDto.getImageUrl())
            .body(
                Question.MultipleChoiceQuestionBody.builder()
                    .answerIndex(0)
                    .choices(List.of(Question.MultipleChoiceQuestionBody.Choice.builder().text("TEST").build()))
                    .build())
            .build();
        quiz = Quiz.builder()
            .id("TEST")
            .title("TEST")
            .createdBy("TEST")
            .dateCreated(Instant.now())
            .lastUpdated(Instant.now())
            .questions(List.of(question))
            .build();
    }

    @Test
    void POST_shouldCreateAndReturnQuiz() throws Exception {
        // Assembler is mocked (not testing actual links added), so just return argument
        when(modelAssembler.toModel(any())).thenAnswer(invocation -> EntityModel.<Quiz>of(invocation.getArgument(0)));

        // QuizService returns the known test value that's converted properly
        when(quizService.create(any(), any())).thenReturn(quiz);

        // Controller should accept a Dto, save it, and return what the service gives it
        val result = mockMvc.perform(
                post("/api/quizzes")
                    .content(objectMapper.writeValueAsString(quizDto))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
            .andReturn();

        // Parse the Quiz using ObjectMapper to get access to the JsonSubTypes features
        val responseQuiz = objectMapper.readValue(result.getResponse().getContentAsString(), Quiz.class);

        // Parsed Quiz from the response should be the same as the one from QuizService
        assertThat(responseQuiz, is(equalTo(quiz)));

        // Verify QuizService received the QuizDto given
        val argCaptor = ArgumentCaptor.forClass(QuizDto.class);
        verify(quizService).create(argCaptor.capture(), any());
        val requestQuizDto = argCaptor.getValue();
        assertThat(requestQuizDto, is(equalTo(quizDto)));
    }

    @Test
    void POST_whenInvalidJson_shouldRespondStatusBadRequest() throws Exception {
        // Feed bad json request (missing title) to endpoint
        var requestBody = Map.<String, Object>ofEntries(
            entry("questions", List.of(questionDto))
        );
        mockMvc.perform(
                post("/api/quizzes")
                    .content(objectMapper.writeValueAsString(requestBody))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errors[0].field").value("title"))
            .andExpect(jsonPath("$.errors[0].value").value("null"));

        // Try with missing questions
        requestBody = new HashMap<>();
        requestBody.put("title", "Title");
        requestBody.put("questions", null);
        mockMvc.perform(
                post("/api/quizzes")
                    .content(objectMapper.writeValueAsString(requestBody))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errors[0].field").value("questions"))
            .andExpect(jsonPath("$.errors[0].value").value("null"));

        // Try with invalid question body
        val invalidBody = ((QuestionDto.MultipleChoiceQuestionBody) questionDto.getBody())
            .toBuilder()
            .answerIndex(-1) // can't be negative
            .build();
        val invalidQuestion = questionDto.toBuilder()
            .body(invalidBody)
            .build();
        requestBody = Map.ofEntries(
            entry("title", "Title"),
            entry("questions", List.of(invalidQuestion))
        );
        mockMvc.perform(
                post("/api/quizzes")
                    .content(objectMapper.writeValueAsString(requestBody))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errors[0].field").value("questions[0].body.answerIndex"))
            .andExpect(jsonPath("$.errors[0].value").value(-1));
    }

    @Test
    void PUT_shouldSaveUpdatesAndReturnQuiz() throws Exception {
        when(modelAssembler.toModel(any())).thenAnswer(invocation -> EntityModel.<Quiz>of(invocation.getArgument(0)));
        when(quizService.save(any(), any())).thenReturn(quiz);

        // Controller should take an updated QuizDto for /api/quizzes/{id}, save updates, and return the updated quiz
        val result = mockMvc.perform(
                put("/api/quizzes/" + quiz.getId())
                    .content(objectMapper.writeValueAsString(quizDto))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
            .andReturn();

        // Response should be the same as return value from the service
        val responseQuiz = objectMapper.readValue(result.getResponse().getContentAsString(), Quiz.class);
        assertThat(responseQuiz, is(equalTo(quiz)));

        // Verify QuizService method was called
        val quizArgCaptor = ArgumentCaptor.forClass(QuizDto.class);
        val idArgCaptor = ArgumentCaptor.forClass(String.class);
        verify(quizService).save(idArgCaptor.capture(), quizArgCaptor.capture());
        assertThat(quizArgCaptor.getValue(), is(equalTo(quizDto)));
        assertThat(idArgCaptor.getValue(), is(equalTo(quiz.getId())));
    }

    @Test
    void DELETE_shouldDeleteQuiz() throws Exception {
        // Controller should delete the quiz with {id} from /api/quizzes/{id}
        mockMvc.perform(
                delete("/api/quizzes/" + quiz.getId())
                    .content(objectMapper.writeValueAsString(quizDto))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        val idArgCaptor = ArgumentCaptor.forClass(String.class);
        verify(quizService).delete(idArgCaptor.capture());
        assertThat(idArgCaptor.getValue(), is(equalTo(quiz.getId())));
    }

    @Test
    void GET_shouldReturnQuizWithoutAnswers() throws Exception {
        when(quizService.findById(any())).thenReturn(Optional.of(quiz));
        when(modelAssembler.toModel(any())).thenAnswer(invocation -> EntityModel.<Quiz>of(invocation.getArgument(0)));

        // Controller should return the quiz with {id} from /api/quizzes/{id}
        val result = mockMvc.perform(
                get("/api/quizzes/" + quiz.getId())
                    .content(objectMapper.writeValueAsString(quizDto))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
            .andReturn();

        // Response should be the same as return value from the service but without answers
        val responseQuiz = objectMapper.readValue(result.getResponse().getContentAsString(), Quiz.class);
        assertThat(responseQuiz, samePropertyValuesAs(quiz, "questions"));
        assertThat(
            (Question.MultipleChoiceQuestionBody) responseQuiz.getQuestions().get(0).getBody(),
            samePropertyValuesAs((Question.MultipleChoiceQuestionBody) quiz.getQuestions().get(0).getBody(), "answerIndex")
        );
        assertThat(
            ((Question.MultipleChoiceQuestionBody) responseQuiz.getQuestions().get(0).getBody()).getAnswerIndex(),
            is(nullValue())
        );

        val idArgCaptor = ArgumentCaptor.forClass(String.class);
        verify(quizService).findById(idArgCaptor.capture());
        assertThat(idArgCaptor.getValue(), is(equalTo(quiz.getId())));
    }
}
