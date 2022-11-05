package com.bobrov.meetup.service.impl;

import com.bobrov.meetup.controller.MeetupController;
import com.bobrov.meetup.dao.MeetupRepository;
import com.bobrov.meetup.dto.MeetupDto;
import com.bobrov.meetup.dto.mapper.MeetupMapper;
import com.bobrov.meetup.model.Meetup;
import com.bobrov.meetup.service.exception.FilterValidationException;
import com.bobrov.meetup.service.exception.NoSuchMeetupException;
import com.bobrov.meetup.service.exception.SortValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MeetupImplTest {
    @Mock
    private MeetupRepository repository;
    @InjectMocks
    private MeetupImpl service;
    private final Map<String, String> paramsForFilter;
    private final List<String> paramsForSort;
    private final String sortOrder;
    private final MeetupDto meetupDto;
    private final Meetup meetup;

    public MeetupImplTest() {
        paramsForFilter = Collections.emptyMap();
        paramsForSort = List.of(MeetupController.DEF_SORT_BY);
        sortOrder = MeetupController.DEF_SORT_ORDER;
        meetupDto = MeetupDto.builder()
                .id(1L)
                .topic("Joker")
                .description("International conference for experienced Java developers")
                .organizer("JUGru group")
                .eventDate(LocalDateTime.now().plusMonths(1))
                .place("Moscow")
                .build();
        meetup = MeetupMapper.INSTANCE.toModel(meetupDto);
    }

    @Test
    @DisplayName("findById: success")
    void findById_success() {
        final Meetup meetup = new Meetup();

        given(repository.findById(1L))
                .willReturn(Optional.of(meetup));

        assertEquals(meetup, service.findById(1L));
    }

    @Test
    @DisplayName("findById: meetup with such id doesn't exist")
    void should_throwNoSuchMeetupEx_when_meetupWithSuchIdIsNotFound() {
        given(repository.findById(1L))
                .willReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(1L))
                .isInstanceOf(NoSuchMeetupException.class)
                .hasMessageContaining("Meetup with id=1 was not found");
    }

    @Test
    void findAllSuccess() {
        given(repository.findAll(paramsForFilter, paramsForSort, sortOrder))
                .willReturn(Collections.emptyList());

        assertEquals(
                Collections.emptyList(),
                service.findAll(paramsForFilter, paramsForSort, sortOrder)
        );
    }

    @Test
    void should_throwSortValidationEx_when_sortOrderIsInvalid() {
        String invalidSortOrder = "asccc";

        assertThatThrownBy(() -> service.findAll(paramsForFilter, paramsForSort, invalidSortOrder))
                .isInstanceOf(SortValidationException.class)
                .hasMessageContaining(String.format("Unknown order direction '%s'", invalidSortOrder));
    }

    @Test
    void should_throwSortValidationEx_when_fieldForSortIsInvalid() {
        String unknownField = "someFieldToSort";
        List<String> badParamsForSort = List.of(unknownField);

        assertThatThrownBy(() -> service.findAll(paramsForFilter, badParamsForSort, sortOrder))
                .isInstanceOf(SortValidationException.class)
                .hasMessageContaining((String.format("Unknown field '%s'", unknownField)));
    }

    @Test
    void should_throwFilterValidationEx_when_filterOperationIsInvalid() {
        String unknownFilterOperation = "SOME_OPERATION";
        Map<String, String> badOperationsForFilter = Map.of("id", unknownFilterOperation);

        assertThatThrownBy(() -> service.findAll(badOperationsForFilter, paramsForSort, sortOrder))
                .isInstanceOf(FilterValidationException.class)
                .hasMessageContaining((String.format("Unknown operation '%s' for filtering", unknownFilterOperation)));
    }

    @Test
    void should_throwFilterValidationEx_when_filterParamIsInvalid() {
        String badParam = "someParam";
        Map<String, String> badParamsForFilter = Map.of(badParam, "gt;2");

        assertThatThrownBy(() -> service.findAll(badParamsForFilter, paramsForSort, sortOrder))
                .isInstanceOf(FilterValidationException.class)
                .hasMessageContaining((String.format("Unknown field '%s'", badParam)));
    }

    @Test
    void saveSuccess() {
        given(repository.saveOrUpdate(meetup))
                .willReturn(meetup);

        assertEquals(meetup, service.save(meetupDto));
    }

    @Test
    void updateSuccess() {
        given(repository.findById(meetup.getId()))
                .willReturn(Optional.of(meetup));
        given(repository.saveOrUpdate(meetup))
                .willReturn(meetup);

        assertEquals(meetup, service.update(meetupDto.getId(), meetupDto));
    }

    @Test
    void deleteByIdSuccess() {
        given(repository.findById(meetup.getId()))
                .willReturn(Optional.of(meetup));

        service.deleteById(meetup.getId());

        verify(repository).delete(meetup);
    }

    @Test
    void should_throwNoSuchMeetupEx_when_deleteMeetupWithNotExistId() {
        Long id = meetup.getId();

        given(repository.findById(id))
                .willReturn(Optional.empty());

        assertThatThrownBy(() -> service.deleteById(id))
                .isInstanceOf(NoSuchMeetupException.class)
                .hasMessageContaining((String.format("Meetup with id=%s was not found", id)));
    }
}