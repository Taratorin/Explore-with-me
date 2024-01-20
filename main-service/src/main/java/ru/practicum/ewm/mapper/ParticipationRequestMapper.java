package ru.practicum.ewm.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.practicum.ewm.dto.ParticipationRequestDto;
import ru.practicum.ewm.model.ParticipationRequest;

@Mapper
public interface ParticipationRequestMapper {

    ParticipationRequestMapper INSTANCE = Mappers.getMapper(ParticipationRequestMapper.class);

    ParticipationRequest participationRequestDtoToParticipationRequest(ParticipationRequestDto participationRequestDto);
    ParticipationRequestDto participationRequestToParticipationRequestDto(ParticipationRequest participationRequest);


}
