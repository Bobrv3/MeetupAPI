package com.bobrov.meetup.service.impl;

import com.bobrov.meetup.dao.MeetupRepository;
import com.bobrov.meetup.dto.MeetupDto;
import com.bobrov.meetup.exception.NoSuchMeetupException;
import com.bobrov.meetup.mapper.MeetupMapper;
import com.bobrov.meetup.model.Meetup;
import com.bobrov.meetup.service.MeetupService;
import com.bobrov.meetup.service.validator.FilterValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MeetupImpl implements MeetupService {
    private final MeetupRepository meetupRepository;

    @Override
    public Meetup findById(Long id) {
        return meetupRepository.findById(id)
                .orElseThrow(() -> new NoSuchMeetupException(id));
    }

    @Override
    public List<Meetup> findAll(Map<String, String> paramsForFilter, List<String> paramsForSort, String sortOrder) {
        FilterValidator.validate(paramsForFilter, Meetup.class);

        return meetupRepository.findAll(paramsForFilter, paramsForSort, sortOrder);
    }

    @Override
    @Transactional
    public Meetup save(Meetup meetup) {
        return meetupRepository.save(meetup);
    }

    @Override
    @Transactional
    public Meetup update(Long id, MeetupDto meetupDto) {
        Meetup meetup = findById(id);
        MeetupMapper.INSTANCE.updateModel(meetupDto, meetup);

        return meetupRepository.update(meetup);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Meetup meetup = findById(id);

        meetupRepository.delete(meetup);
    }
}
