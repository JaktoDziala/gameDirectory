package com.gameManagement.exceptions;

public class NullObjectException extends RuntimeException {
    public NullObjectException(Class message){super("Accessed " + message + " object is null!");}
}
