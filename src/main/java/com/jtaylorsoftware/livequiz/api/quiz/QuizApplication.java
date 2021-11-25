package com.jtaylorsoftware.livequiz.api.quiz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;

@SpringBootApplication
public class QuizApplication {

	public static void main(String[] args) {
		System.out.println("Region: " + System.getenv("AWS_REGION"));
		System.out.println(Arrays.toString(args));
		SpringApplication.run(QuizApplication.class, args);
	}

}
