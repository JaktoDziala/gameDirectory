package com.gameDirectory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

/**
 * TODO: Non urgent:
 *      TODO: Replace platform and genre single type of game into collection;
 *
 */
@SpringBootApplication
@EntityScan("com.gameDirectory")
public class GameDirectoryApplication {
    // TODO: Check what happens when param is in form of request body vs param.
    // TODO X: Add controller tests for Validating fields. It should replace service validation DTO to controller
    public static void main(String[] args) {
        SpringApplication.run(GameDirectoryApplication.class, args);
    }

}


