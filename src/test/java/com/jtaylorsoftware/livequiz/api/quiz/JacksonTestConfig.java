package com.jtaylorsoftware.livequiz.api.quiz;

import lombok.val;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@TestConfiguration
public class JacksonTestConfig {
    @Bean
    Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder(){
        val builder = new Jackson2ObjectMapperBuilder();
        // Enable serialization of properties that don't have view annotations
        // (default mapper provided sets this to false, breaking HATEOAS features)
        builder.defaultViewInclusion(true);
        return builder;
    }
}