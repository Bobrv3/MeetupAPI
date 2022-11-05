package com.bobrov.meetup.service.impl;

import com.bobrov.meetup.config.DataHandler;
import com.bobrov.meetup.dto.MeetupDto;
import com.bobrov.meetup.service.MeetupService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import javax.validation.ConstraintViolationException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@AutoConfigureTestDatabase
@ContextConfiguration(classes = DataHandler.class)
class MeetupImplFailedBeanValidationTest {
    private MeetupService service;
    private MeetupDto meetupDto;

    @Autowired
    public MeetupImplFailedBeanValidationTest(@Qualifier("meetupDtoWithoutId") MeetupDto meetupDto, MeetupService service) {
        this.meetupDto = meetupDto;
        this.service = service;

        meetupDto.setOrganizer(null);
        meetupDto.setTopic("  ");
    }

    @Test
    @DisplayName("findById: not valid id")
    void findById() {
        assertThatThrownBy(() -> service.findById(0L))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("id: должно быть не меньше 1");
    }

    @Test
    @DisplayName("save: not valid meetupDto")
    void save() {
        assertThatThrownBy(() -> service.save(meetupDto))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("topic: не должно быть пустым")
                .hasMessageContaining("organizer: не должно быть пустым");
    }

    @Test
    @DisplayName("update: not valid meetupDto and id")
    void update() {
        assertThatThrownBy(() -> service.update(0L, meetupDto))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("id: должно быть не меньше 1")
                .hasMessageContaining("topic: не должно быть пустым")
                .hasMessageContaining("organizer: не должно быть пустым");
    }

    @Test
    @DisplayName("deleteById: not valid id")
    void deleteById() {
        assertThatThrownBy(() -> service.deleteById(0L))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("id: должно быть не меньше 1");
    }
}