package com.gameDirectory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan("com.gameDirectory")
public class GameDirectoryApplication {
	// TODO: Possible interface introduction, check how it help with encapsulation
	// TODO: Design patterns, find suitable for the project. Now or later?
	// TODO: Check saving Lists to DB as json string
	// TODO: Add controller tests for Validating fields. It should replace service validation DTO to controller
	public static void main(String[] args) {
		SpringApplication.run(GameDirectoryApplication.class, args);
	}

}
