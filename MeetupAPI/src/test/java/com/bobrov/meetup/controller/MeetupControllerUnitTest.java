package com.bobrov.meetup.controller;

import com.bobrov.meetup.config.DataHandler;
import com.bobrov.meetup.dto.MeetupDto;
import com.bobrov.meetup.dto.mapper.MeetupMapper;
import com.bobrov.meetup.model.Meetup;
import com.bobrov.meetup.service.MeetupService;
import com.bobrov.meetup.service.exception.FilterValidationException;
import com.bobrov.meetup.service.exception.NoSuchMeetupException;
import com.bobrov.meetup.service.exception.SortValidationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import javax.validation.ConstraintViolationException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@ContextConfiguration(classes = DataHandler.class)
class MeetupControllerUnitTest {
    @MockBean
    private MeetupService service;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    @Qualifier("meetups")
    private List<Meetup> meetups;

    @Autowired
    @Qualifier("meetupDtoWithId1")
    private MeetupDto meetupDtoWithId1;

    @Autowired
    @Qualifier("meetupDtoWithoutId")
    private MeetupDto meetupDtoWithoutId;

    @Autowired
    @Qualifier("meetupWithId1")
    private Meetup meetupWithId1;

    private Map<String, String>  paramsForFilter = Collections.emptyMap();
    private List<String> paramsForSort = List.of(MeetupController.DEF_SORT_BY);
    private String sortOrder = MeetupController.DEF_SORT_ORDER;

    @Test
    @DisplayName("getByID: success")
    void getById_Success() throws Exception {
        when(service.findById(1L))
                .thenReturn(meetupWithId1);

        this.mockMvc.perform(get("/api/meetups/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(meetupDtoWithId1)));
    }

    @Test
    @DisplayName("getByID: meetup with such id doesn't found")
    void should_returnNoSuchMeetupEx_if_meetupNotFound() throws Exception {
        when(service.findById(10L))
                .thenThrow(new NoSuchMeetupException(10L));

        this.mockMvc.perform(get("/api/meetups/10"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("NoSuchMeetupException")))
                .andExpect(content().string(containsString("Meetup with id=10 was not found")));
    }

    @Test
    @DisplayName("getByID: not valid id")
    void should_returnExceptionResponseWithMessageForId_if_idIsInvalid() throws Exception {
        when(service.findById(0L))
                .thenThrow(new ConstraintViolationException("id: must be greater than or equal to 1", null));

        this.mockMvc.perform(get("/api/meetups/0"))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(containsString("ConstraintViolationException")))
                .andExpect(content().string(containsString("id: must be greater than or equal to 1")));
    }

    @Test
    @DisplayName("getByID: incorrect id")
    void should_returnNotFoundStatus_if_idIsIncorrect() throws Exception {
        this.mockMvc.perform(get("/api/meetups/abc"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("getAll: success default sorting and filtering")
    void getAll_defaultSortingAndFiltering_success() throws Exception {
        when(service.findAll(paramsForFilter, paramsForSort, sortOrder))
                .thenReturn(meetups);

        this.mockMvc.perform(get("/api/meetups"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(MeetupMapper.INSTANCE.toListDto(meetups))));
    }

    @Test
    @DisplayName("getAll: unknown sorting order")
    void should_returnSortValidationEx_if_unknownSortOrder() throws Exception {
        String unknownSortOrder = "desccccc";

        when(service.findAll(Map.of("sort_order", "desccccc"), paramsForSort, unknownSortOrder))
                .thenThrow(new SortValidationException(String.format("Unknown order direction '%s'", unknownSortOrder)));

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

        when(service.findAll(Map.of("sort_by", "eventDateeeeee"), List.of("eventDateeeeee"), sortOrder))
                .thenThrow(new SortValidationException(String.format("Unknown field '%s'", unknownField)));

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

        when(service.findAll(Map.of("pricee", "eq;JUGru group"), paramsForSort, sortOrder))
                .thenThrow(new FilterValidationException(String.format("Unknown field '%s'", unknownField)));

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

        when(service.findAll(Map.of("eventDate", "GTRERER;2022-11-11T11:00"), paramsForSort, sortOrder))
                .thenThrow(new FilterValidationException(String.format("Unknown operation '%s' for filtering", unknownOperation)));

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

        when(service.findAll(Map.of("eventDate", "gt;2022fdfs-11-11T11:00"), paramsForSort, sortOrder))
                .thenThrow(new FilterValidationException(String.format("not correct value for filtering '%s'", incorrectValue)));

        this.mockMvc.perform(get("/api/meetups")
                        .param("eventDate", "gt;" + incorrectValue))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString(String.format("not correct value for filtering '%s'", incorrectValue))))
                .andExpect(content().string(containsString("FilterValidationException")));
    }

    @Test
    @DisplayName("createMeetup: success")
    void createMeetup_success() throws Exception {
        when(service.save(meetupDtoWithoutId))
                .thenAnswer(mock -> {
                    meetupDtoWithoutId.setId(5L);
                    Meetup meetup = MeetupMapper.INSTANCE.toModel(meetupDtoWithoutId);

                    return meetup;
                });

        this.mockMvc.perform(post("/api/meetups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(meetupDtoWithoutId)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/api/meetups/5"))
                .andExpect(content().json(objectMapper.writeValueAsString(meetupDtoWithoutId)));
    }

    @Test
    @DisplayName("createMeetup: not valid meetup")
    void should_returnConstraintViolationEx_if_meetupIsNotValid() throws Exception {
        meetupDtoWithoutId.setTopic("  ");
        meetupDtoWithoutId.setOrganizer(null);

        when(service.save(meetupDtoWithoutId))
                .thenThrow(new ConstraintViolationException("topic: must not be blank, organizer: must not be blank", null));

        this.mockMvc.perform(post("/api/meetups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(meetupDtoWithoutId)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(containsString("ConstraintViolationException")))
                .andExpect(content().string(containsString("topic: must not be blank")))
                .andExpect(content().string(containsString("organizer: must not be blank")));
    }

    @Test
    @DisplayName("updateMeetup: success")
    void updateMeetup_success() throws Exception {
        when(service.update(4L, meetupDtoWithId1))
                .thenAnswer(mock -> {
                    meetupDtoWithId1.setId(4L);
                    Meetup meetupWithId4 = MeetupMapper.INSTANCE.toModel(meetupDtoWithId1);
                    meetupDtoWithId1.setId(1L);

                    return meetupWithId4;
                });

        this.mockMvc.perform(put("/api/meetups/4")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(meetupDtoWithId1)))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("updateMeetup: not valid id and meetup")
    void should_returnConstraintViolationEx_if_IdOrMeetupIsIncorrect() throws Exception {
        meetupDtoWithoutId.setOrganizer(null);

        when(service.update(0L, meetupDtoWithoutId))
                .thenThrow(new ConstraintViolationException("id: must be greater than or equal to 1, organizer: must not be blank", null));

        this.mockMvc.perform(put("/api/meetups/0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(meetupDtoWithoutId)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(containsString("ConstraintViolationException")))
                .andExpect(content().string(containsString("organizer: must not be blank")))
                .andExpect(content().string(containsString("id: must be greater than or equal to 1")));
    }

    @Test
    @DisplayName("updateMeetup: meetup with such id doesn't exist")
    void should_returnNoSuchMeetupEx_if_updateAndMeetupIsNotFound() throws Exception {
        when(service.update(10L, meetupDtoWithoutId))
                .thenThrow(new NoSuchMeetupException(10L));

        this.mockMvc.perform(put("/api/meetups/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(meetupDtoWithoutId)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("NoSuchMeetupException")))
                .andExpect(content().string(containsString("Meetup with id=10 was not found")));
    }

    @Test
    @DisplayName("deleteById: success")
    void deleteById() throws Exception {
        this.mockMvc.perform(delete("/api/meetups/4"))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(service).deleteById(4L);
    }
}