package com.bobrov.meetup.controller;

import com.bobrov.meetup.dto.ExceptionResponse;
import com.bobrov.meetup.service.exception.NoSuchMeetupException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class ExceptionHandlerController {

    @ExceptionHandler(NoSuchMeetupException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponse handleException(NoSuchMeetupException exception) {
        log.error(exception.getMessage(), exception);

        return ExceptionResponse.builder()
                .message(exception.getMessage())
                .type(exception.getClass().getSimpleName())
                .createdAt(LocalDateTime.now())
                .statusCode(HttpStatus.NOT_FOUND.value())
                .build();
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ExceptionResponse handleException(ConstraintViolationException exception) {
        log.error(exception.getMessage(), exception);

        return ExceptionResponse.builder()
                .message(exception.getMessage())
                .type(exception.getClass().getSimpleName())
                .createdAt(LocalDateTime.now())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .build();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionResponse handleException(Exception exception) {
        log.error(exception.getMessage(), exception);

        return ExceptionResponse.builder()
                .message(exception.getMessage())
                .type(exception.getClass().getSimpleName())
                .createdAt(LocalDateTime.now())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .build();
    }
}
