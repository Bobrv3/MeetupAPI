package com.bobrov.meetup.exception;

public class NoSuchMeetupException extends RuntimeException {
    private Long id;

    public NoSuchMeetupException(Long id) {
        this.id = id;
    }

    @Override
    public String getMessage() {
        return String.format("Meetup with id=%s was not found", id);
    }
}
