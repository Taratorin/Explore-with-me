package ru.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.EventShortDto;
import ru.practicum.dto.NewEventDto;
import ru.practicum.dto.UpdateEventUserRequest;
import ru.practicum.model.Event;

@Mapper
public interface EventMapper {

    EventMapper INSTANCE = Mappers.getMapper(EventMapper.class);

    Event newEventDtoToEvent(NewEventDto newEventDto);
    EventFullDto eventToEventFullDto(Event event);

    EventShortDto eventToEventShortDto(Event event);

    Event updateEventUserRequestToEvent(UpdateEventUserRequest updateEventUserRequest);

}
