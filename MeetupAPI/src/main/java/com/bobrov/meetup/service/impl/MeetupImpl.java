package com.bobrov.meetup.service.impl;

import com.bobrov.meetup.dao.MeetupRepository;
import com.bobrov.meetup.dto.MeetupDto;
import com.bobrov.meetup.dto.mapper.MeetupMapper;
import com.bobrov.meetup.model.Meetup;
import com.bobrov.meetup.service.MeetupService;
import com.bobrov.meetup.service.exception.NoSuchMeetupException;
import com.bobrov.meetup.service.validator.FilterValidator;
import com.bobrov.meetup.service.validator.SortValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Validated
public class MeetupImpl implements MeetupService {
    private final MeetupRepository meetupRepository;

    @Override
    public Meetup findById(@Min(1) Long id) {
        return meetupRepository.findById(id)
                .orElseThrow(() -> new NoSuchMeetupException(id));
    }

    @Override
    public List<Meetup> findAll(Map<String, String> paramsForFilter, List<String> paramsForSort, String sortOrder) {
        FilterValidator.validate(paramsForFilter, Meetup.class);
        SortValidator.validate(paramsForSort, sortOrder, Meetup.class);

        return meetupRepository.findAll(paramsForFilter, paramsForSort, sortOrder);
    }

    @Override
    @Transactional
    public Meetup save(@Valid MeetupDto meetupDto) {
        return meetupRepository.saveOrUpdate(
                MeetupMapper.INSTANCE.toModel(meetupDto)
        );
    }

    @Override
    @Transactional
    public Meetup update(@Min(1) Long id, @Valid MeetupDto meetupDto) {
        Meetup meetup = findById(id);
        MeetupMapper.INSTANCE.updateModel(meetupDto, meetup);

        return meetupRepository.saveOrUpdate(meetup);
    }

    @Override
    @Transactional
    public void deleteById(@Min(1) Long id) {
        Meetup meetup = findById(id);

        meetupRepository.delete(meetup);
    }
}
