package ru.skypro.homework.exception.file;

public class FileSavingException extends RuntimeException {

    public FileSavingException() {
    }

    public FileSavingException(String message) {
        super(message);
    }
}
