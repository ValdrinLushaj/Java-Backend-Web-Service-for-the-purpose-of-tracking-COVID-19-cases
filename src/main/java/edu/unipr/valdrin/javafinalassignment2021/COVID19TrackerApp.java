package edu.unipr.valdrin.javafinalassignment2021;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@EnableScheduling 		//The @EnableScheduling annotation is used to enable the scheduler for your application. This annotation should be added into the main Spring Boot application class file. The @Scheduled annotation is used to trigger the scheduler for a specific time period
public class COVID19TrackerApp {

	public static void main(String[] args) {
		SpringApplication.run(COVID19TrackerApp.class, args);
	}

}
