package com.bobrov.meetup.controller;

import com.bobrov.meetup.config.DataConfig;
import com.bobrov.meetup.dto.MeetupDto;
import com.bobrov.meetup.model.Meetup;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@Import(DataConfig.class)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MeetupControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MeetupController controller;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private List<Meetup> meetups;
    private MeetupDto meetupDto = MeetupDto.builder()
            .place("Minsk")
            .topic("OpenIT")
            .eventDate(LocalDateTime.now().plusYears(1))
            .organizer("IT Academy")
            .description("IT conference for beginners")
            .build();

    @Test
    @DisplayName("getByID: success")
    void getById_Success() throws Exception {
        this.mockMvc.perform(get("/api/meetups/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(meetups.get(0))));
    }

    @Test
    @DisplayName("getByID: not correct id")
    void should_returnExceptionResponseWithMessageForId() throws Exception {
        this.mockMvc.perform(get("/api/meetups/0"))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(containsString("id: must be greater than or equal to 1")));
    }

    @Test
    @DisplayName("getByID: invalid id")
    void should_returnNotFoundError() throws Exception {
        this.mockMvc.perform(get("/api/meetups/abc"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("getAll: default sorting and filtering success")
    void getAll_defaultSortingAndFiltering_Success() throws Exception {
        this.mockMvc.perform(get("/api/meetups"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(meetups)));
    }

    @Test
    @DisplayName("getAll: custom sort and filter by existing fields and order success")
    void getAll_withCustomSortingAndFilteringByExistingFields_Success() throws Exception {
        List<Meetup> meetupsCopy = new ArrayList<>(meetups);

        List<Meetup> filteredList = meetupsCopy.stream()
                .filter(meetup -> meetup.getEventDate().isAfter(LocalDateTime.parse("2022-11-11T11:00")))
                .sorted(Comparator.comparing(Meetup::getPlace)
                        .thenComparing(Meetup::getTopic))
                .collect(Collectors.toList());

        Collections.reverse(filteredList);

        this.mockMvc.perform(get("/api/meetups")
                        .param("eventDate", "gt;2022-11-11T11:00")
                        .param("sort_order", "desc")
                        .param("sort_by", "place", "topic"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(filteredList)));
    }

    @Test
    @DisplayName("getAll: unknown sorting order")
    void should_returnSortValidationEx_if_unknownSortOrder() throws Exception {
        String unknownSortOrder = "desccccc";

        this.mockMvc.perform(get("/api/meetups")
                        .param("sort_order", unknownSortOrder))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString(String.format("Unknown order direction '%s'", unknownSortOrder))))
                .andExpect(content().string(containsString("SortValidationException")));
    }

    @Test
    @DisplayName("getAll: sort by not existing field")
    void should_returnSortValidationEx_if_suchFieldDoesNotExist() throws Exception {
        String unknownField = "eventDateeeeee";

        this.mockMvc.perform(get("/api/meetups")
                        .param("sort_by", unknownField))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString(String.format("Unknown field '%s'", unknownField))))
                .andExpect(content().string(containsString("SortValidationException")));
    }

    @Test
    @DisplayName("getAll: not existing field for filter")
    void should_returnFilterValidationEx_when_filterFieldDoesNotExist() throws Exception {
        String unknownField = "pricee";

        this.mockMvc.perform(get("/api/meetups")
                        .param(unknownField, "eq;JUGru group"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString(String.format("Unknown field '%s'", unknownField))))
                .andExpect(content().string(containsString("FilterValidationException")));
    }

    @Test
    @DisplayName("getAll: not existing operation for filter")
    void should_returnFilterValidationEx_when_filterOperationDoesNotExist() throws Exception {
        String unknownOperation = "GTRERER";

        this.mockMvc.perform(get("/api/meetups")
                        .param("eventDate", unknownOperation + ";2022-11-11T11:00"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString(String.format("Unknown operation '%s' for filtering", unknownOperation))))
                .andExpect(content().string(containsString("FilterValidationException")));
    }

    @Test
    @DisplayName("getAll: not correct value for filter")
    void should_returnFilterValidationEx_when_filterValueIsNotCorrect() throws Exception {
        String incorrectValue = "2022fdfs-11-11T11:00";

        this.mockMvc.perform(get("/api/meetups")
                        .param("eventDate", "gt;" + incorrectValue))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString(String.format("not correct value for filtering '%s'", incorrectValue))))
                .andExpect(content().string(containsString("FilterValidationException")));
    }

    @Test
    @DisplayName("createMeetup: success")
    @Order(1)
    void createMeetup_success() throws Exception {
        this.mockMvc.perform(post("/api/meetups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(meetupDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/api/meetups/5"))
                .andExpect(content().string(containsString("OpenIT")));
    }

    @Test
    @DisplayName("createMeetup: meetup is not valid")
    void should_returnConstraintViolationEx_if_meetupIsNotValid() throws Exception {
        String topic = meetupDto.getTopic();
        String organizer = meetupDto.getOrganizer();

        meetupDto.setTopic("  ");
        meetupDto.setOrganizer(null);

        this.mockMvc.perform(post("/api/meetups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(meetupDto)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(containsString("topic: must not be blank")))
                .andExpect(content().string(containsString("organizer: must not be blank")));

        meetupDto.setTopic(topic);
        meetupDto.setOrganizer(organizer);
    }

    @Test
    @DisplayName("updateMeetup: success")
    @Order(2)
    void updateMeetup_success() throws Exception {
        this.mockMvc.perform(put("/api/meetups/5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(meetupDto)))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("updateMeetup: incorrect id and meetup")
    void should_returnConstraintViolationEx_ifIdOrMeetupIsIncorrect() throws Exception {
        String organizer = meetupDto.getOrganizer();
        meetupDto.setOrganizer(null);

        this.mockMvc.perform(put("/api/meetups/0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(meetupDto)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(containsString("ConstraintViolationException")))
                .andExpect(content().string(containsString("organizer: must not be blank")))
                .andExpect(content().string(containsString("id: must be greater than or equal to 1")));

        meetupDto.setOrganizer(organizer);
    }

    @Test
    @DisplayName("updateMeetup: meetup with such id is not found")
    void should_returnNoSuchMeetupEx_if_updateAndMeetupIsNotFound() throws Exception {
        this.mockMvc.perform(put("/api/meetups/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(meetupDto)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("NoSuchMeetupException")))
                .andExpect(content().string(containsString("Meetup with id=10 was not found")));
    }

    @Test
    @DisplayName("deleteById: success")
    @Order(5)
    void deleteById() throws Exception {
        this.mockMvc.perform(delete("/api/meetups/5"))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("deleteById: meetup with such id is not found")
    void should_returnNoSuchMeetupEx_if_deleteAndMeetupIsNotFound() throws Exception {
        this.mockMvc.perform(delete("/api/meetups/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(meetupDto)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("NoSuchMeetupException")))
                .andExpect(content().string(containsString("Meetup with id=10 was not found")));
    }
}