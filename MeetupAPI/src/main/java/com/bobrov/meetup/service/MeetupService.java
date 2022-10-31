package com.bobrov.meetup.service;

import com.bobrov.meetup.dto.MeetupDto;
import com.bobrov.meetup.model.Meetup;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.Map;

public interface MeetupService {
    Meetup findById(@Min(1) Long id);
    List<Meetup> findAll(Map<String, String> paramsForFilter, List<String> paramsForSort, String sortOrder);
    Meetup save(@Valid MeetupDto meetupDto);
    Meetup update(@Min(1) Long id, @Valid MeetupDto meetupDto);
    void deleteById(@Min(1) Long id);
}
