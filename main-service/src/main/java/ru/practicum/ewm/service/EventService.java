package ru.practicum.ewm.service;

import io.micrometer.core.lang.Nullable;
import ru.practicum.ewm.dto.*;
import ru.practicum.ewm.model.SortType;
import ru.practicum.ewm.model.State;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    EventFullDto saveNewEvent(NewEventDto newEventDto, long userId);

    List<EventShortDto> getEvents(long userId, int from, int size);

    EventFullDto getEvent(long userId, long eventId);

    EventFullDto patchEventUser(long userId, long eventId, UpdateEventUserRequest updateEventUserRequest);

    List<ParticipationRequestDto> getEventRequests(long userId, long eventId);

    EventRequestStatusUpdateResult patchEventRequests(long userId, long eventId, EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest);

    List<EventFullDto> findEventsByConditions(@Nullable List<Long> userIds,
                                              @Nullable List<State> states, @Nullable List<Long> categories,
                                              @Nullable LocalDateTime rangeStart, @Nullable LocalDateTime rangeEnd,
                                              int from, int size);

    EventFullDto patchEventAdmin(long eventId, UpdateEventAdminRequest updateEventAdminRequest);

    List<EventShortDto> getEventsPublic(String text, List<Long> categories, Boolean paid, Boolean onlyAvailable, LocalDateTime rangeStart, LocalDateTime rangeEnd, SortType sort, int from, int size);

    EventFullDto getEventPublic(long id);
}
