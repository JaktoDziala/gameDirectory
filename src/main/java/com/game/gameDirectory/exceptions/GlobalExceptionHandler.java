package com.game.gameDirectory.exceptions;

import com.game.gameDirectory.game.Game;
import com.game.gameDirectory.game.GameRepository;
import com.game.gameDirectory.review.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @Deprecated
    @Autowired
    GameRepository gameRepository;

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<?> handleObjectNotFoundException(ObjectNotFoundException e) {
        // Casual Heresy
        return new ResponseEntity<>(e.getMessage() +
                ". Nearest game id: " + gameRepository.findAll().getFirst().getId(),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(GamePlatformNotFoundException.class)
    public ResponseEntity<?> handleGamePlatformNotFoundException(GamePlatformNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(OutOfBoundsRatingException.class)
    public ResponseEntity<?> handleOutOfBoundsRatingException(OutOfBoundsRatingException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

}




// @ControllerAdvice(basePackages = "com.example.myPackage")
// @ControllerAdvice(annotations = RestController.class)
// @ControllerAdvice(assignableTypes = {MyController1.class, MyController2.class})
