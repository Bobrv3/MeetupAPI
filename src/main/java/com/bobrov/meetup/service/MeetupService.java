package com.bobrov.meetup.service;

import com.bobrov.meetup.dto.MeetupDto;
import com.bobrov.meetup.model.Meetup;

import java.util.List;

public interface MeetupService {
    Meetup findById(Long id);
    List<Meetup> findAll();
    Meetup save(Meetup meetup);
    Meetup update(Long id, MeetupDto meetupDto);
    void deleteById(Long id);
}
