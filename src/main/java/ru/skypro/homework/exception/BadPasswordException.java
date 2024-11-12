package ru.skypro.homework.exception;

public class BadPasswordException extends RuntimeException {

    public BadPasswordException() {
    }

    public BadPasswordException(String message) {
        super(message);
    }
}
