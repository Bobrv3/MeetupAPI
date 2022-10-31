package com.bobrov.meetup.dao;

import com.bobrov.meetup.model.Meetup;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface MeetupRepository {
    Optional<Meetup> findById(Long id);
    List<Meetup> findAll(Map<String, String> paramsForFilter, List<String> params, String sortOrder);
    Meetup saveOrUpdate(Meetup meetup);
    void delete(Meetup meetup);
}
