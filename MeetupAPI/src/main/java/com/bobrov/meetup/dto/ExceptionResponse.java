package com.bobrov.meetup.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class ExceptionResponse {
    private String message;
    private String type;
    private int statusCode;
    private LocalDateTime createdAt;
}
