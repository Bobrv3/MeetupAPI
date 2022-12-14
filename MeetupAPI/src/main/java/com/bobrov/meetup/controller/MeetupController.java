package com.bobrov.meetup.controller;

import com.bobrov.meetup.dto.MeetupDto;
import com.bobrov.meetup.dto.mapper.MeetupMapper;
import com.bobrov.meetup.model.Meetup;
import com.bobrov.meetup.service.MeetupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/meetups")
@RequiredArgsConstructor
public class MeetupController {
    public static final String SORT_ORDER = "sort_order";
    public static final String DEF_SORT_ORDER = "asc";
    public static final String SORT_BY = "sort_by";
    public static final String DEF_SORT_BY = "id";
    private final MeetupService meetupService;

    @GetMapping("/{id:\\d+}")
    public MeetupDto getById(@PathVariable Long id) {
        return MeetupMapper.INSTANCE.toDto(
                meetupService.findById(id)
        );
    }

    @GetMapping
    public List<MeetupDto> getAll(
            @RequestParam(required = false, name = SORT_ORDER, defaultValue = DEF_SORT_ORDER) String sortOrder,
            @RequestParam(required = false, name = SORT_BY,defaultValue = DEF_SORT_BY) List<String> paramsForSort,
            @RequestParam(required = false) Map<String, String> paramsForFilter
    ) {
        return MeetupMapper.INSTANCE.toListDto(
                meetupService.findAll(paramsForFilter, paramsForSort, sortOrder)
        );
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> createMeetup(@RequestBody MeetupDto meetupDto) {
        Meetup meetup = meetupService.save(meetupDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(meetup.getId())
                .toUri();

        return ResponseEntity
                .created(uri)
                .body(MeetupMapper.INSTANCE.toDto(meetup));
    }

    @PutMapping("/{id:\\d+}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateMeetup(
            @RequestBody MeetupDto meetupDto,
            @PathVariable Long id
    ) {
        meetupService.update(id, meetupDto);
    }

    @DeleteMapping("/{id:\\d+}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) {
        meetupService.deleteById(id);
    }
}
