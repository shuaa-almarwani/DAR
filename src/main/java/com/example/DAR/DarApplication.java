package com.example.DAR;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
@EnableScheduling
@SpringBootApplication

public class DarApplication {

	public static void main(String[] args) {
		SpringApplication.run(DarApplication.class, args);
	}

}
