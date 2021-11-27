package com.jtaylorsoftware.livequiz.api.quiz.dao;

import com.jtaylorsoftware.livequiz.api.quiz.model.Quiz;
import lombok.val;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.UpdateItemEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
import software.amazon.awssdk.services.dynamodb.model.ResourceInUseException;

import static software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional.keyEqualTo;

@Component
public class QuizDaoImpl implements QuizDao {
    private DynamoDbTable<Quiz> quizTable;

    public QuizDaoImpl(DynamoDbEnhancedClient ddbClient) {
        try {
            // Try to create the table because it may not exist on first run
            quizTable = ddbClient.table("quizzes_table", TableSchema.fromBean(Quiz.class));
            quizTable.createTable();
        } catch (ResourceInUseException e) {
            // Already exists, which is fine
            System.out.println(e.getMessage());
        } catch (DynamoDbException e) {
            // Unknown exception
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    @Override
    public Boolean existsById(String id) {
        try {
            val results = quizTable.query(keyEqualTo(Key.builder().partitionValue(id).build()));
            return results.items().stream().findAny().isPresent();
        } catch (DynamoDbException e) {
            throw new DatabaseException(e);
        }
    }

    public Quiz find(Quiz quiz) {
        try {
            return quizTable.getItem(quiz);
        } catch (DynamoDbException e) {
            throw new DatabaseException(e);
        }
    }

    public void create(Quiz quiz) {
        try {
            quizTable.putItem(quiz);
        } catch (DynamoDbException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public Quiz save(Quiz quiz) {
        try {
            return quizTable.updateItem(UpdateItemEnhancedRequest.builder(Quiz.class).ignoreNulls(true).build());
        } catch (DynamoDbException e) {
            throw new DatabaseException(e);
        }
    }

    public void delete(Quiz quiz) {
        try {
            quizTable.deleteItem(quiz);
        } catch (DynamoDbException e) {
            throw new DatabaseException(e);
        }
    }
}
