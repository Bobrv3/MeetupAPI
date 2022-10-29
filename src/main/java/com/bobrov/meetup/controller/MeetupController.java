package com.bobrov.meetup.controller;

import com.bobrov.meetup.dto.MeetupDto;
import com.bobrov.meetup.mapper.MeetupMapper;
import com.bobrov.meetup.service.MeetupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/meetups")
@RequiredArgsConstructor
public class MeetupController {
    private final MeetupService meetupService;

    @GetMapping("/{id:\\d+}")
    public MeetupDto getById(@PathVariable Long id) {
        return MeetupMapper.INSTANCE.toDto(
                meetupService.findById(id)
        );
    }

    @GetMapping
    public List<MeetupDto> getAll() {
        return MeetupMapper.INSTANCE.toListDto(
                meetupService.findAll()
        );
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MeetupDto saveMeetup(@Valid @RequestBody MeetupDto meetupDto) {
        return MeetupMapper.INSTANCE.toDto(
                meetupService.save(MeetupMapper.INSTANCE.toModel(meetupDto))
        );
    }

    @PutMapping("/{id:\\d+}")
    public MeetupDto updateMeetup(
            @Valid @RequestBody MeetupDto meetupDto,
            @PathVariable Long id) {
        return MeetupMapper.INSTANCE.toDto(
                meetupService.update(id, meetupDto)
        );
    }

    @DeleteMapping("/{id:\\d+}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) {
        meetupService.deleteById(id);
    }
}
