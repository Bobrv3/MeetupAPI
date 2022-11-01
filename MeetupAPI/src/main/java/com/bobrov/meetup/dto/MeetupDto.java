package com.bobrov.meetup.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Builder
@AllArgsConstructor
public class MeetupDto implements Serializable {
    @Min(value = 0)
    private final Long id;

    @NotBlank
    private String topic;

    @NotBlank
    private String description;

    @NotBlank
    private String organizer;

    @Future
    @NotNull
    private LocalDateTime eventDate;

    @NotBlank
    private String place;
}