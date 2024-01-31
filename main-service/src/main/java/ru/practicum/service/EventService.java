package ru.practicum.service;

import ru.practicum.dto.*;
import ru.practicum.model.SortType;
import ru.practicum.model.State;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    EventFullDto saveNewEvent(NewEventDto newEventDto, long userId);

    List<EventShortDto> getEvents(long userId, int from, int size);

    EventFullDto getEvent(long userId, long eventId);

    EventFullDto patchEventUser(long userId, long eventId, UpdateEventUserRequest updateEventUserRequest);

    List<ParticipationRequestDto> getEventRequests(long userId, long eventId);

    EventRequestStatusUpdateResult patchEventRequests(long userId, long eventId, EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest);

    List<EventFullDto> findEventsByConditions(List<Long> userIds,
                                              List<State> states, List<Long> categories,
                                              LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                              int from, int size, HttpServletRequest request);

    EventFullDto patchEventAdmin(long eventId, UpdateEventAdminRequest updateEventAdminRequest);

    List<EventShortDto> getEventsPublic(String text, List<Long> categories, boolean paid, boolean onlyAvailable, LocalDateTime rangeStart, LocalDateTime rangeEnd, SortType sort, int from, int size, HttpServletRequest request);

    EventFullDto findEventPublic(Long id, HttpServletRequest request);
}
