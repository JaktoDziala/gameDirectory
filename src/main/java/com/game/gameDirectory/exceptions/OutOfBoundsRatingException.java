package com.game.gameDirectory.exceptions;

public class OutOfBoundsRatingException extends RuntimeException{
    public OutOfBoundsRatingException (int rating){super("Rating of " + rating + " is not within range of 1 to 10");}
}
