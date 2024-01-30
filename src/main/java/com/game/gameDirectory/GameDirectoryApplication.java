package com.game.gameDirectory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan("com.game.gameDirectory")
public class GameDirectoryApplication {
	// TODO: Possible interface introduction, check how it help with encapsulation
	// TODO: Design patterns, find suitable for the project. Now or later?
	// TODO: Check saving Lists to DB as json string
	public static void main(String[] args) {
		SpringApplication.run(GameDirectoryApplication.class, args);
	}

}
