package ru.practicum.ewm.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.*;
import ru.practicum.ewm.exception.BadRequestException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.mapper.EventMapper;
import ru.practicum.ewm.mapper.ParticipationRequestMapper;
import ru.practicum.ewm.model.*;
import ru.practicum.ewm.repository.EventRepository;
import ru.practicum.ewm.repository.ParticipationRequestRepository;
import ru.practicum.ewm.repository.UserRepository;
import ru.practicum.ewm.service.EventService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.practicum.ewm.config.Constants.DATE_TIME_FORMATTER;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final ParticipationRequestRepository participationRequestRepository;

    @Override
    public EventFullDto saveNewEvent(NewEventDto newEventDto, long userId) {
        Event event = EventMapper.INSTANCE.newEventDtoToEvent(newEventDto);
        Event savedEvent = eventRepository.save(event);
        return EventMapper.INSTANCE.eventToEventFullDto(savedEvent);
    }

    @Override
    public List<EventShortDto> getEvents(long userId, int from, int size) {
        User user = findUserById(userId);
        int pageNumber = from / size;
        Pageable pageable = PageRequest.of(pageNumber, size);
        List<Event> eventsByUser = eventRepository.findAllByInitiator(user, pageable);
        return eventsByUser.stream().map(EventMapper.INSTANCE::eventToEventShortDto).collect(Collectors.toList());
    }

    @Override
    public EventFullDto getEvent(long userId, long eventId) {
        User initiator = findUserById(userId);
        Event event = findEventByIdAndUser(eventId, initiator);
        return EventMapper.INSTANCE.eventToEventFullDto(event);
    }

    @Override
    public EventFullDto patchEvent(long userId, long eventId, UpdateEventUserRequest updateEventUserRequest) {
        User initiator = findUserById(userId);
        Event event = findEventByIdAndUser(eventId, initiator);
        if (event.getState().equals(State.CANCELED) || event.getState().equals(State.PENDING)) {
            updateEvent(updateEventUserRequest, event);
            Event savedEvent = eventRepository.save(event);
            return EventMapper.INSTANCE.eventToEventFullDto(savedEvent);
        }
        return EventMapper.INSTANCE.eventToEventFullDto(event);
    }

    @Override
    public List<ParticipationRequestDto> getEventRequests(long userId, long eventId) {
        User initiator = findUserById(userId);
        Event event = findEventById(eventId);
        if (event.getInitiator() != initiator) {
            throw new BadRequestException("Для получения информации вы должны быть инициатором события.");
        }
        List<ParticipationRequest> participationRequests = participationRequestRepository.findByEvent(event);
        return participationRequests.stream()
                .map(ParticipationRequestMapper.INSTANCE::participationRequestToParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventRequestStatusUpdateResult patchEventRequests
            (long userId, long eventId, EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
// todo учесть требования
//        todo остановился
        //        если для события лимит заявок равен 0 или отключена пре-модерация заявок, то подтверждение заявок не требуется
//        нельзя подтвердить заявку, если уже достигнут лимит по заявкам на данное событие (Ожидается код ошибки 409)
//        статус можно изменить только у заявок, находящихся в состоянии ожидания (Ожидается код ошибки 409)
//        если при подтверждении данной заявки, лимит заявок для события исчерпан, то все неподтверждённые заявки необходимо отклонить
        String statusString = eventRequestStatusUpdateRequest.getStatus();
        Status status = Status.valueOf(statusString);
        User initiator = findUserById(userId);
        Event event = findEventById(eventId);
        if (event.getInitiator() != initiator) {
            throw new BadRequestException("Для изменения статуса заявок вы должны быть инициатором события.");
        }
        List<Long> ids = eventRequestStatusUpdateRequest.getRequestIds();
        List<ParticipationRequest> participationRequests = participationRequestRepository.findByEventAndIdIn(event, ids);
        for (ParticipationRequest participationRequest : participationRequests) {
            participationRequest.setStatus(status);
        }
        participationRequestRepository.saveAll(participationRequests);
        return null;
    }

    private User findUserById(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id=" + userId + " не существует."));
    }

    private Event findEventByIdAndUser(long eventId, User initiator) {
        return eventRepository.findEventByIdAndInitiator(eventId, initiator)
                .orElseThrow(() -> new NotFoundException("Мероприятие с id=" + eventId +
                        " у текущего пользователя не существует."));
    }

    private Event findEventById(long eventId) {
        return eventRepository.findEventById(eventId)
                .orElseThrow(() -> new NotFoundException("Мероприятие с id=" + eventId + " не найдено."));
    }

    private void updateEvent(UpdateEventUserRequest updateEventUserRequest, Event event) {
        if (!updateEventUserRequest.getAnnotation().equals(event.getAnnotation())) {
            event.setAnnotation(updateEventUserRequest.getAnnotation());
        }
        if (!updateEventUserRequest.getCategory().getId().equals(event.getCategory().getId())) {
            event.setId(updateEventUserRequest.getCategory().getId());
        }
        if (!updateEventUserRequest.getDescription().equals(event.getDescription())) {
            event.setDescription(updateEventUserRequest.getDescription());
        }
        String eventDateString = updateEventUserRequest.getEventDate();
        LocalDateTime eventDate = LocalDateTime.parse(eventDateString, DATE_TIME_FORMATTER);
        if (!eventDate.equals(event.getEventDate())) {
            event.setEventDate(eventDate);
        }
        if (updateEventUserRequest.getLocation().getLat() != event.getLocationLat()
                || updateEventUserRequest.getLocation().getLon() != event.getLocationLon()) {
            event.setLocationLat(updateEventUserRequest.getLocation().getLat());
            event.setLocationLon(updateEventUserRequest.getLocation().getLon());
        }
        if (updateEventUserRequest.isPaid() && !event.isPaid()) {
            event.setPaid(updateEventUserRequest.isPaid());
        }
        if (updateEventUserRequest.getParticipantLimit() != event.getParticipantLimit()) {
            event.setParticipantLimit(updateEventUserRequest.getParticipantLimit());
        }
        if (updateEventUserRequest.isRequestModeration() && !event.isRequestModeration()) {
            event.setRequestModeration(updateEventUserRequest.isRequestModeration());
        }
        if (updateEventUserRequest.getStateAction().equals(StateAction.SEND_TO_REVIEW.toString())) {
            event.setState(State.PENDING);
        }
        if (updateEventUserRequest.getStateAction().equals(StateAction.CANCEL_REVIEW.toString())) {
            event.setState(State.CANCELED);
        }
        if (!updateEventUserRequest.getTitle().equals(event.getTitle())) {
            event.setTitle(updateEventUserRequest.getTitle());
        }
    }
}
