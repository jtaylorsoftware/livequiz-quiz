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

import static software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional.keyEqualTo;

@Component
public class QuizDaoImpl implements QuizDao {
    private DynamoDbTable<Quiz> quizTable;

    public QuizDaoImpl(DynamoDbEnhancedClient ddbClient)
    {
        try {
            quizTable = ddbClient.table("quizzes_table", TableSchema.fromBean(Quiz.class));
            quizTable.createTable();
        } catch(DynamoDbException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }

    @Override
    public Boolean existsById(String id) {
        val results = quizTable.query(keyEqualTo(Key.builder().partitionValue(id).build()));
        return results.items().stream().findAny().isPresent();
    }

    public Quiz find(Quiz quiz) {
        return quizTable.getItem(quiz);
    }

    public Quiz create(Quiz quiz) {
        quizTable.putItem(quiz);
        return quiz;
    }

    @Override
    public Quiz save(Quiz quiz) {
        return quizTable.updateItem(UpdateItemEnhancedRequest.builder(Quiz.class).ignoreNulls(true).build());
    }

    public void delete(Quiz quiz) {
        quizTable.deleteItem(quiz);
    }
}
