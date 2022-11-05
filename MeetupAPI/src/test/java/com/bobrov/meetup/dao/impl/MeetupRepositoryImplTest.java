package com.bobrov.meetup.dao.impl;

import com.bobrov.meetup.config.DataHandler;
import com.bobrov.meetup.model.Meetup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@AutoConfigureTestDatabase
@ContextConfiguration(classes = DataHandler.class)
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MeetupRepositoryImplTest {
    @Autowired
    private MeetupRepositoryImpl repository;

    @Autowired
    @Qualifier("meetups")
    private List<Meetup> meetups;

    @Autowired
    @Qualifier("meetupWithId1")
    private Meetup meetupWithId1;

    @Autowired
    @Qualifier("meetupWithoutId")
    private Meetup meetupWithoutId;

    @Test
    @DisplayName("findById: success")
    void findById_success() {
        assertEquals(meetupWithId1, repository.findById(1L).get());
    }

    @Test
    @DisplayName("findById: meetup with such id doesn't found")
    void should_returnEmptyOptional_if_meetupNotFound() {
        assertEquals(Optional.empty(), repository.findById(10L));
    }

    @Test
    @DisplayName("findAll: success findAll sorting desc by organizer, topic where eventDate grater than 2022-11-11T11:00")
    void findAll_success() {
        List<Meetup> meetupsCopy = new ArrayList<>(meetups);

        List<Meetup> filteredList = meetupsCopy.stream()
                .filter(meetup -> meetup.getEventDate().isAfter(LocalDateTime.parse("2022-11-11T11:00")))
                .sorted(Comparator.comparing(Meetup::getOrganizer)
                        .thenComparing(Meetup::getTopic))
                .collect(Collectors.toList());

        Collections.reverse(filteredList);

        assertEquals(
                filteredList,
                repository.findAll(Map.of("eventDate", "gt;2022-11-11T11:00"), List.of("organizer", "topic"), "desc")
        );

    }

    @Test
    @DisplayName("saveOrUpdate: success save")
    @Order(1)
    void saveOrUpdate_successSave() {
        Meetup meetup = Meetup.builder()
                .topic("Joker")
                .place("Moscow")
                .description("International conference for experienced Java developers")
                .organizer("JUGru group")
                .eventDate(LocalDateTime.parse("2022-11-19T09:30"))
                .build();

        Meetup saved = repository.saveOrUpdate(meetup);

        assertEquals(5L, saved.getId());
    }

    @Test
    @DisplayName("saveOrUpdate: attempt to save meetup with null fields in the db")
    @Order(2)
    void should_throw() {
        Meetup meetup = Meetup.builder()
                .topic(null)
                .place("Moscow")
                .description("International conference for experienced Java developers")
                .organizer("JUGru group")
                .eventDate(LocalDateTime.parse("2022-11-19T09:30"))
                .build();

        assertThatThrownBy(() -> {
            repository.saveOrUpdate(meetup);
            repository.flush();
        })
                .isInstanceOf(DataIntegrityViolationException.class)
                .hasMessageContaining("constraint [null]");
    }

    @Test
    @DisplayName("saveOrUpdate: success update")
    void saveOrUpdate_successUpdate() {
        Meetup meetup = Meetup.builder()
                .id(1L)
                .topic("New topic")
                .place("Moscow")
                .description("International conference for experienced Java developers")
                .organizer("JUGru group")
                .eventDate(LocalDateTime.parse("2022-11-19T09:30"))
                .build();

        Meetup saved = repository.saveOrUpdate(meetup);

        assertEquals(1L, saved.getId());
        assertEquals("New topic", saved.getTopic());
    }

    @Test
    void delete() {
        Meetup meetup = repository.saveOrUpdate(meetupWithId1);

        assertTrue(repository.delete(meetup));
    }
}