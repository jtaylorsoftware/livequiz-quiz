package com.jtaylorsoftware.livequiz.api.quiz.dao;

import com.jtaylorsoftware.livequiz.api.quiz.model.Question;
import com.jtaylorsoftware.livequiz.api.quiz.model.Quiz;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.net.URI;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class QuizDaoTests {
    private QuizDao quizDao;

    private DynamoDbEnhancedClient createClient() {
        val environment = System.getProperty("environment");
        val endpoint = environment.equals("local") ? "http://localhost:8000" : "http://dynamodb:8000";
        val dynamoDbClient = DynamoDbClient.builder()
            .endpointOverride(URI.create(endpoint))
            .region(Region.US_WEST_2)
            .credentialsProvider(StaticCredentialsProvider.create(
                AwsBasicCredentials.create("dummy-key", "dummy-secret")
            ))
            .build();
        return new DynamoDbClientConfigDevTest().dynamoDbEnhancedClient(dynamoDbClient);
    }

    @BeforeEach
    void setup() {
        quizDao = new QuizDaoImpl(createClient());
    }

    @Test
    void find_whenNoQuizzes_returnsNull() {
        assertThat(quizDao.find(Quiz.builder().build()), is(nullValue()));
    }

    @Test
    void create_canBeFoundById_andHasSavedValues() {
        val quiz = Quiz.builder()
            .title("Title")
            .createdBy("Test")
            .build();
        quizDao.create(quiz);
        val foundQuiz = quizDao.find(quiz);
        assertThat(foundQuiz.getId(), is(equalTo(quiz.getId())));
        assertThat(foundQuiz.getTitle(), is(equalTo(quiz.getTitle())));
        assertThat(foundQuiz.getCreatedBy(), is(equalTo(quiz.getCreatedBy())));
        // Should have lastUpdated populated by dao
        assertThat(
            foundQuiz.getLastUpdated().atZone(ZoneId.systemDefault()).toLocalDate(),
            is(equalTo(LocalDate.now()))
        );
    }

    @Test
    void save_returnsUpdated_hasNewPropValues() {
        val quiz = Quiz.builder()
            .title("Title")
            .createdBy("Test")
            .build();
        quizDao.create(quiz);
        val questions = List.of(
            new Question(
                "Prompt",
                "image",
                new Question.MultipleChoiceQuestionBody(
                    Question.QuestionType.MULTIPLE_CHOICE,
                    List.of(new Question.MultipleChoiceQuestionBody.Choice("Question")),
                    0
                )
            )
        );
        val updatedQuiz = quiz.toBuilder()
            .title("Edited")
            .questions(questions)
            .build();
        val savedQuiz = quizDao.save(updatedQuiz);
        // Id should be retained
        assertThat(savedQuiz.getId(), is(equalTo(updatedQuiz.getId())));
        // All other fields should be updated, and lastUpdated should be set by dao
        assertThat(savedQuiz.getLastUpdated(), not(equalTo(quiz.getLastUpdated())));
        assertThat(savedQuiz.getTitle(), is(equalTo(updatedQuiz.getTitle())));
        assertThat(savedQuiz.getQuestions(), contains(updatedQuiz.getQuestions().toArray()));
        // Should be able to find the savedQuiz using its id and get the same props back
        val foundSavedQuiz = quizDao.find(Quiz.builder().id(savedQuiz.getId()).build());
        assertThat(foundSavedQuiz, is(equalTo(savedQuiz)));
    }

    @Test
    void save_doesNotUpdateNulls() {
        val quiz = Quiz.builder()
            .title("Title")
            .createdBy("Test")
            .build();
        quizDao.create(quiz);
        val updatedQuiz = quiz.toBuilder()
            .title(null)
            .createdBy("Edited")
            .build();
        val savedQuiz = quizDao.save(updatedQuiz);
        // Saved quiz should retain original title because it's null
        assertThat(savedQuiz.getTitle(), is(equalTo(quiz.getTitle())));
        // Saved quiz should have a new createdBy because it's not null
        assertThat(savedQuiz.getCreatedBy(), is(equalTo(updatedQuiz.getCreatedBy())));
    }

    @Test
    void delete_quizExists_doesDelete() {
        val quiz = Quiz.builder()
            .title("Title")
            .createdBy("Test")
            .build();
        quizDao.create(quiz);
        // Ensure it was created
        var foundQuiz = quizDao.find(quiz);
        assertThat(foundQuiz, is(notNullValue()));

        // Delete and ensure it no longer exists
        quizDao.delete(quiz);
        foundQuiz = quizDao.find(quiz);
        assertThat(foundQuiz, is(nullValue()));
    }
}
