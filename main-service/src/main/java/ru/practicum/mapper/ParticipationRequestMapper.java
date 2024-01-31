package ru.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.practicum.dto.ParticipationRequestDto;
import ru.practicum.model.ParticipationRequest;

@Mapper
public interface ParticipationRequestMapper {

    ParticipationRequestMapper INSTANCE = Mappers.getMapper(ParticipationRequestMapper.class);

    @Mapping(target = "event", source = "event.id")
    @Mapping(target = "requester", source = "requester.id")
    @Mapping(target = "created", dateFormat = "yyyy-MM-dd HH:mm:ss")
    ParticipationRequestDto participationRequestToParticipationRequestDto(ParticipationRequest participationRequest);


}
