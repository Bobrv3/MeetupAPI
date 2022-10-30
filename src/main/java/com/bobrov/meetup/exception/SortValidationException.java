package com.bobrov.meetup.exception;

public class SortValidationException extends RuntimeException {
    public SortValidationException() {
        super();
    }

    public SortValidationException(String message) {
        super(message);
    }

    public SortValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public SortValidationException(Throwable cause) {
        super(cause);
    }
}
