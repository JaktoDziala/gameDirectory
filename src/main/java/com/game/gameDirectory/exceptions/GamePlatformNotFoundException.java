package com.game.gameDirectory.exceptions;

public class GamePlatformNotFoundException extends RuntimeException{
    GamePlatformNotFoundException(String message){
        super(message);
    }
}
