package com.bobrov.meetup.dao;

import com.bobrov.meetup.model.Meetup;

import java.util.List;
import java.util.Optional;

public interface MeetupDAO {
    Optional<Meetup> findById(Long id);
    List<Meetup> findAll();
    Meetup save(Meetup meetup);
    Meetup update(Meetup meetup);
    void deleteById(Long id);
}
