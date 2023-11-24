package com.game.gameDirectory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan("com.game.gameDirectory")
public class GameDirectoryApplication {

	public static void main(String[] args) {
		SpringApplication.run(GameDirectoryApplication.class, args);
	}

}
