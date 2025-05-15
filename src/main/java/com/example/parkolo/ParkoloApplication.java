package com.example.parkolo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ParkoloApplication {

	public static void main(String[] args) {
		SpringApplication.run(ParkoloApplication.class, args);
	}
}