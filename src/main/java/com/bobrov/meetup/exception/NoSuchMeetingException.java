package com.bobrov.meetup.exception;

public class NoSuchMeetingException extends RuntimeException {
    public NoSuchMeetingException() {
    }

    public NoSuchMeetingException(String message) {
        super(message);
    }

    public NoSuchMeetingException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSuchMeetingException(Throwable cause) {
        super(cause);
    }
}
