package com.bobrov.meetup.dto.mapper;

import com.bobrov.meetup.dto.MeetupDto;
import com.bobrov.meetup.model.Meetup;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface MeetupMapper {
    MeetupMapper INSTANCE = Mappers.getMapper(MeetupMapper.class);

    Meetup toModel(MeetupDto meetupDto);
    MeetupDto toDto(Meetup meetup);
    void updateModel(MeetupDto meetupDto, @MappingTarget Meetup meetup);

    List<MeetupDto> toListDto(List<Meetup> byId);
}
