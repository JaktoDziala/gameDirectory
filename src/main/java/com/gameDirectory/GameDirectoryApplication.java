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
    // TODO: Possible interface introduction, check how it help with encapsulation
    // TODO: Design patterns, find suitable for the project. Now or later?
    // TODO: Check saving Lists to DB as json string
    // TODO: Check what happens when param is in form of request body vs param.
    public static void main(String[] args) {
        SpringApplication.run(GameDirectoryApplication.class, args);
    }

}


