package com.gameDirectory.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<?> handleObjectNotFoundException(ObjectNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(GamePlatformNotFoundException.class)
    public ResponseEntity<?> handleGamePlatformNotFoundException(GamePlatformNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(OutOfBoundsRatingException.class)
    public ResponseEntity<?> handleOutOfBoundsRatingException(OutOfBoundsRatingException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NullObjectException.class)
    public ResponseEntity<?> handleNullObjectException(NullObjectException e){
    return new ResponseEntity<>(e.getMessage(), HttpStatus.NO_CONTENT);
    }

    @ExceptionHandler(InvalidDateFormatException.class)
    public ResponseEntity<?> handleInvalidDateFormatException(InvalidDateFormatException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidDTOValueException.class)
    public ResponseEntity<?> handleInvalidDTOValueException(InvalidDTOValueException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}




// @ControllerAdvice(basePackages = "com.example.myPackage")
// @ControllerAdvice(annotations = RestController.class)
// @ControllerAdvice(assignableTypes = {MyController1.class, MyController2.class})
