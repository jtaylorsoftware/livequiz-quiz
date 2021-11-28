package com.jtaylorsoftware.livequiz.api.quiz.dao;

import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.net.URI;

@Configuration
@Profile("!prod")
public class DynamoDbClientConfigDevTest {
    @Value("${environment}")
    private String environment;

    @Bean
    public DynamoDbClient dynamoDbClient() {
        val endpoint = environment.equals("local") ? "http://localhost:8000" : "http://dynamodb:8000";
        return DynamoDbClient.builder()
            .endpointOverride(URI.create(endpoint))
            .region(Region.US_WEST_2)
            .credentialsProvider(StaticCredentialsProvider.create(
                AwsBasicCredentials.create("dummy-key", "dummy-secret")
            ))
            .build();
    }

    @Bean
    public DynamoDbEnhancedClient dynamoDbEnhancedClient(DynamoDbClient dynamoDbClient) {
        return DynamoDbEnhancedClient.builder().dynamoDbClient(dynamoDbClient).build();
    }
}
