package com.jtaylorsoftware.livequiz.api.quiz;

import lombok.val;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;

@SpringBootApplication
public class QuizApplication {
    public static void main(String[] args) {
        val applicationContext = SpringApplication.run(QuizApplication.class, args);
        val profiles = applicationContext.getEnvironment().getActiveProfiles();
        if (Arrays.stream(profiles).anyMatch(profile -> profile.equals("dev") || profile.equals("test"))) {
            System.out.println(Arrays.toString(args));
        }
    }
}
