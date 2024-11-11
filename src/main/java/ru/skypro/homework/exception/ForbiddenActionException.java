package ru.skypro.homework.exception;

public class ForbiddenActionException extends RuntimeException {

    public ForbiddenActionException(String message) {
        super(message);
    }

    public ForbiddenActionException() {
    }
}
