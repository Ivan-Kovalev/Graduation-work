package ru.skypro.homework.exception;

public class UserIsAlreadyExistException extends RuntimeException {

    public UserIsAlreadyExistException() {
    }

    public UserIsAlreadyExistException(String message) {
        super(message);
    }
}
