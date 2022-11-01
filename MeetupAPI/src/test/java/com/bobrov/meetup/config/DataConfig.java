package com.bobrov.meetup.config;

import com.bobrov.meetup.model.Meetup;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.util.List;

@TestConfiguration
public class DataConfig {
    @Bean
    public List<Meetup> meetups() {
        return List.of(
                Meetup.builder()
                        .id(1L)
                        .topic("Joker")
                        .place("Moscow")
                        .description("International conference for experienced Java developers")
                        .organizer("JUGru group")
                        .eventDate(LocalDateTime.parse("2022-11-19T09:30")).build(),

                Meetup.builder()
                        .id(2L)
                        .topic("Mobius 2022 Autumn")
                        .place("Moscow")
                        .description("Conference for mobile developers")
                        .organizer("JUGru group")
                        .eventDate(LocalDateTime.parse("2022-11-21T10:00")).build(),

                Meetup.builder()
                        .id(3L)
                        .topic("DUMP Kazan 2022")
                        .place("Kazan")
                        .description("Tatarstan's central IT conference")
                        .organizer("IT People Connection")
                        .eventDate(LocalDateTime.parse("2022-11-11T11:00")).build(),

                Meetup.builder()
                        .id(4L)
                        .topic("Joker")
                        .place("Moscow")
                        .description("International conference for experienced Java developers")
                        .organizer("Skolkovo Innovation Center")
                        .eventDate(LocalDateTime.parse("2023-11-25T10:00")).build()
                );
    }
}
