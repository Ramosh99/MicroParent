package org.example.Exceptions;

public class NoUserException extends RuntimeException {
    public NoUserException(String message) {
        super(message);
    }
}