package ru.skypro.homework.exception.file;

import java.io.IOException;

public class FileSavingException extends RuntimeException {

    public FileSavingException() {
    }

    public FileSavingException(String message) {
        super(message);
    }

}
