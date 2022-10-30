package com.bobrov.meetup.controller;

import com.bobrov.meetup.exception.ExceptionResponse;
import com.bobrov.meetup.exception.NoSuchMeetupException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    // TODO comment
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ExceptionResponse handleException(MethodArgumentNotValidException exception) {
        log.error(exception.getMessage(), exception);

        Matcher matcher = Pattern.compile("(?<=default message \\[)[\\w\\s\\W]*?(?=\\])")
                .matcher(exception.getMessage());

        StringBuilder message = new StringBuilder("");
        while (matcher.find()) {
            message.append(matcher.group())
                    .append(":");
        }

        return ExceptionResponse.builder()
                .message(message.substring(0, message.lastIndexOf(":")))
                .type(exception.getClass().getSimpleName())
                .createdAt(LocalDateTime.now())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .build();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
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
