package com.bobrov.meetup.dto;

import lombok.Data;

import javax.validation.constraints.Future;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * A DTO for the {@link com.bobrov.meetup.model.Meetup} entity
 */
@Data
public class MeetupDto implements Serializable {
    @Min(value = 0)
    private final Long id;

    @NotBlank
    private final String topic;

    @NotBlank
    private final String description;

    @NotBlank
    private final String organizer;

    @Future
    @NotNull
    private final LocalDateTime eventDate;

    @NotBlank
    private final String place;
}