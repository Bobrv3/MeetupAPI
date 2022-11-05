package com.bobrov.meetup.dto.mapper;

import com.bobrov.meetup.config.DataHandler;
import com.bobrov.meetup.dto.MeetupDto;
import com.bobrov.meetup.model.Meetup;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = DataHandler.class)
class MeetupMapperTest {
    @Autowired
    @Qualifier("meetupWithId1")
    private Meetup meetupWithId1;

    @Autowired
    @Qualifier("meetupDtoWithId1")
    private MeetupDto meetupDtoWithId1;

    @Test
    void toModel() {
        Meetup model = MeetupMapper.INSTANCE.toModel(meetupDtoWithId1);

        assertEquals(model.getId(), meetupDtoWithId1.getId());
        assertEquals(model.getOrganizer(), meetupDtoWithId1.getOrganizer());
        assertEquals(model.getPlace(), meetupDtoWithId1.getPlace());
        assertEquals(model.getEventDate(), meetupDtoWithId1.getEventDate());
        assertEquals(model.getTopic(), meetupDtoWithId1.getTopic());
        assertEquals(model.getDescription(), meetupDtoWithId1.getDescription());
    }

    @Test
    void toDto() {
        MeetupDto dto = MeetupMapper.INSTANCE.toDto(meetupWithId1);

        assertEquals(meetupWithId1.getId(), dto.getId());
        assertEquals(meetupWithId1.getOrganizer(), dto.getOrganizer());
        assertEquals(meetupWithId1.getPlace(), dto.getPlace());
        assertEquals(meetupWithId1.getEventDate(), dto.getEventDate());
        assertEquals(meetupWithId1.getTopic(), dto.getTopic());
        assertEquals(meetupWithId1.getDescription(), dto.getDescription());
    }

    @Test
    void updateModel() {
        meetupDtoWithId1.setTopic("New Topic");

        MeetupMapper.INSTANCE.updateModel(meetupDtoWithId1, meetupWithId1);

        assertEquals("New Topic", meetupDtoWithId1.getTopic());
        assertEquals(meetupWithId1.getId(), meetupDtoWithId1.getId());
        assertEquals(meetupWithId1.getOrganizer(), meetupDtoWithId1.getOrganizer());
        assertEquals(meetupWithId1.getPlace(), meetupDtoWithId1.getPlace());
        assertEquals(meetupWithId1.getEventDate(), meetupDtoWithId1.getEventDate());
        assertEquals(meetupWithId1.getDescription(), meetupDtoWithId1.getDescription());
    }

    @Test
    void toListDto() {
        List<Meetup> meetups = List.of(meetupWithId1, meetupWithId1);

        List<MeetupDto> meetupDtos = MeetupMapper.INSTANCE.toListDto(meetups);

        assertEquals(meetupDtos.size(), meetups.size());
        assertEquals(meetupDtos.get(0).getId(), meetups.get(0).getId());
        assertEquals(meetupDtos.get(1).getId(), meetups.get(1).getId());
    }
}