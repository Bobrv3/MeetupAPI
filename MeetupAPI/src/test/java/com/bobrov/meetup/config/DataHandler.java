package com.bobrov.meetup.config;

import com.bobrov.meetup.dto.MeetupDto;
import com.bobrov.meetup.model.Meetup;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

import java.time.LocalDateTime;
import java.util.List;

@TestConfiguration
public class DataHandler {
    public DataHandler() {
        System.out.println("test call");
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
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

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public MeetupDto meetupDtoWithId1() {
        return MeetupDto.builder()
                .id(1L)
                .topic("Joker")
                .place("Moscow")
                .description("International conference for experienced Java developers")
                .organizer("JUGru group")
                .eventDate(LocalDateTime.parse("2022-11-19T09:30")).build();
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public MeetupDto meetupDtoWithoutId() {
        return MeetupDto.builder()
                .topic("Joker")
                .place("Moscow")
                .description("International conference for experienced Java developers")
                .organizer("JUGru group")
                .eventDate(LocalDateTime.parse("2022-11-19T09:30")).build();
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public Meetup meetupWithId1() {
        return Meetup.builder()
                .id(1L)
                .topic("Joker")
                .place("Moscow")
                .description("International conference for experienced Java developers")
                .organizer("JUGru group")
                .eventDate(LocalDateTime.parse("2022-11-19T09:30")).build();
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public Meetup meetupWithoutId() {
        return Meetup.builder()
                .topic("Joker")
                .place("Moscow")
                .description("International conference for experienced Java developers")
                .organizer("JUGru group")
                .eventDate(LocalDateTime.parse("2022-11-19T09:30")).build();
    }
}
