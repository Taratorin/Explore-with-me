package ru.practicum.service.impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.hibernate.HibernateQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.dto.*;
import ru.practicum.ewm.client.stats.StatsClient;
import ru.practicum.ewm.dto.stats.ViewStatsDto;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.EventMapper;
import ru.practicum.mapper.ParticipationRequestMapper;
import ru.practicum.model.*;
import ru.practicum.repository.CategoryRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.ParticipationRequestRepository;
import ru.practicum.repository.UserRepository;
import ru.practicum.service.EventService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final ParticipationRequestRepository participationRequestRepository;
    private final CategoryRepository categoryRepository;
    private final StatsClient statsClient;


    @Override
    public EventFullDto saveNewEvent(NewEventDto newEventDto, long userId) {
        User user = findUserById(userId);
        Event event = EventMapper.INSTANCE.newEventDtoToEvent(newEventDto);
        event.setConfirmedRequests(0);
        event.setLat(newEventDto.getLocation().getLat());
        event.setLon(newEventDto.getLocation().getLon());
        event.setInitiator(user);
        event.setState(State.PENDING);
        event.setCreatedOn(LocalDateTime.now());
        event.setViews(0);
        Optional<Category> optionalCategory = categoryRepository.findById(newEventDto.getCategory());
        optionalCategory.ifPresent(event::setCategory);
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
    public EventFullDto patchEventUser(long userId, long eventId, UpdateEventUserRequest updateEventUserRequest) {
        User initiator = findUserById(userId);
        Event event = findEventByIdAndUser(eventId, initiator);
        if (event.getState().equals(State.PUBLISHED)) {
            throw new ConflictException("Событие уже опубликовано.");
        }
        if (UpdateEventUserRequest.StateAction.CANCEL_REVIEW.equals(updateEventUserRequest.getStateAction())
                && event.getState().equals(State.PENDING)) {
            event.setState(State.CANCELED);
        } else if (UpdateEventUserRequest.StateAction.SEND_TO_REVIEW.equals(updateEventUserRequest.getStateAction())
                && event.getState().equals(State.CANCELED)) {
            event.setState(State.PENDING);
        }
        EventMapper.INSTANCE.updateEventFromDto(updateEventUserRequest, event);
        eventRepository.save(event);
        return EventMapper.INSTANCE.eventToEventFullDto(event);
    }

    @Override
    public EventFullDto patchEventAdmin(long eventId, UpdateEventAdminRequest updateEventAdminRequest) {
        Event event = findEventById(eventId);
        if (event.getState().equals(State.PUBLISHED) || event.getState().equals(State.CANCELED)) {
            throw new ConflictException("Событие уже опубликовано");
        }
        if (UpdateEventAdminRequest.StateAction.REJECT_EVENT.equals(updateEventAdminRequest.getStateAction()) &&
                event.getState().equals(State.PENDING)) {
            event.setState(State.CANCELED);
        } else if (UpdateEventAdminRequest.StateAction.PUBLISH_EVENT.equals(updateEventAdminRequest.getStateAction()) &&
                event.getState().equals(State.PENDING)) {
            event.setState(State.PUBLISHED);
        }
        EventMapper.INSTANCE.updateEventFromDto(updateEventAdminRequest, event);
        Long newCategory = updateEventAdminRequest.getCategory();
        if (newCategory != null && !Objects.equals(event.getCategory().getId(), newCategory)) {
            Category category = categoryRepository.findById(newCategory)
                    .orElseThrow(() -> new NotFoundException("Категория с id=" + newCategory + " не найдена."));
            event.setCategory(category);
        }
        Event savedEvent = eventRepository.save(event);
        return EventMapper.INSTANCE.eventToEventFullDto(savedEvent);
    }

    @Override
    public List<EventShortDto> getEventsPublic(String text, List<Long> categories, boolean paid,
                                               boolean onlyAvailable, LocalDateTime rangeStart,
                                               LocalDateTime rangeEnd, SortType sort, int from, int size,
                                               HttpServletRequest httpServletRequest) {
        statsClient.saveHit(httpServletRequest, LocalDateTime.now());
        List<Event> availableEvents;
        if (categories != null) {
            availableEvents = eventRepository.findEventByCategoryIdInAndState(categories, State.PUBLISHED);
        } else {
            availableEvents = List.of();
        }
        if (availableEvents.isEmpty()) {
            throw new BadRequestException("Event must be published");
        }
        QEvent event = QEvent.event;
        QParticipationRequest request = QParticipationRequest.participationRequest;
        HibernateQuery<?> query = new HibernateQuery<Void>();
        query.from(event, request);
        List<BooleanExpression> conditions = new ArrayList<>();
        if (text != null) {
            conditions.add(event.annotation.containsIgnoreCase(text).or(event.description.containsIgnoreCase(text)));
        }
        if (!categories.isEmpty()) {
            conditions.add(event.category.id.in(categories));
        }
        if (paid) {
            conditions.add(event.paid.eq(true));
        }
        if (rangeStart != null) {
            conditions.add(event.eventDate.after(rangeStart));
        }
        if (rangeEnd != null) {
            conditions.add(event.eventDate.before(rangeEnd));
        }
        if (rangeStart == null && rangeEnd == null) {
            conditions.add(event.eventDate.after(LocalDateTime.now()));
        }
        Sort sortBy = null;
        if (sort != null) {
            if (SortType.EVENT_DATE.equals(sort)) {
                sortBy = Sort.by(Sort.Direction.ASC, "eventDate");
            } else if (SortType.VIEWS.equals(sort)) {
                sortBy = Sort.by(Sort.Direction.ASC, "views");
            }
        } else {
            sortBy = Sort.by(Sort.Direction.ASC, "id");
        }
        int pageNumber = from / size;
        PageRequest pageRequest = PageRequest.of(pageNumber, size, sortBy);
        Optional<BooleanExpression> optionalCondition = conditions.stream().reduce(BooleanExpression::and);
        BooleanExpression finalCondition = optionalCondition.get();
        Page<Event> eventsPage = eventRepository.findAll(finalCondition, pageRequest);
        List<Event> events = eventsPage.stream().collect(Collectors.toList());
        if (onlyAvailable) {
            for (Event e : events) {
                Integer confirmedRequests = participationRequestRepository.countConfirmedRequests(e.getId());
                if (confirmedRequests == e.getParticipantLimit()) {
                    events.remove(e);
                }
            }
        }
        List<EventShortDto> eventShortDtos = new ArrayList<>();
        for (Event e : events) {
            eventShortDtos.add(EventMapper.INSTANCE.eventToEventShortDto(e));
        }
        return eventShortDtos;
    }

    @Override
    public EventFullDto findEventPublic(Long id, HttpServletRequest httpServletRequest) {
        Event event = eventRepository.findByIdAndState(id, State.PUBLISHED)
                .orElseThrow(() -> new NotFoundException("Опубликованное мероприятие с id=" + id + " не найдено."));
        List<ViewStatsDto> viewStatsDtos = statsClient.get(event.getUri(), event.getCreatedOn(), LocalDateTime.now());
        if (viewStatsDtos.isEmpty()) {
            event.setViews(0L);
        } else {
            event.setViews(viewStatsDtos.get(0).getHits());
        }
        statsClient.saveHit(httpServletRequest, LocalDateTime.now());
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
    public EventRequestStatusUpdateResult patchEventRequests(
            long userId, long eventId,
            EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        User initiator = findUserById(userId);
        Event event = findEventById(eventId);
        Integer confirmed = participationRequestRepository.countConfirmedRequests(eventId);
        if (confirmed == null) {
            confirmed = 0;
        }
        event.setConfirmedRequests(confirmed);
        if (event.getInitiator() != initiator) {
            throw new BadRequestException("Для изменения статуса заявок вы должны быть инициатором события.");
        }
        if (event.getParticipantLimit() == event.getConfirmedRequests()) {
            throw new ConflictException("Лимит подтверждённых заявок на мероприятие исчерпан.");
        }
        EventRequestStatusUpdateResult result = new EventRequestStatusUpdateResult();
        List<Long> ids = new ArrayList<>(eventRequestStatusUpdateRequest.getRequestIds());
        List<ParticipationRequest> participationRequests = participationRequestRepository.findByEventAndIdIn(event, ids);
        for (ParticipationRequest participationRequest : participationRequests) {
            if (!Status.PENDING.equals(participationRequest.getStatus())) {
                throw new ConflictException("Статус можно изменить только у заявок, находящихся в состоянии ожидания");
            }
            EventRequestStatusUpdateRequest.Status status = eventRequestStatusUpdateRequest.getStatus();
            if (event.getParticipantLimit() > confirmed &&
                    EventRequestStatusUpdateRequest.Status.CONFIRMED.equals(status)) {
                participationRequest.setStatus(Status.CONFIRMED);
                event.setConfirmedRequests(confirmed + 1);
                result.getConfirmedRequests()
                        .add(ParticipationRequestMapper.INSTANCE
                                .participationRequestToParticipationRequestDto(participationRequest));
            } else {
                participationRequest.setStatus(Status.REJECTED);
                result.getRejectedRequests()
                        .add(ParticipationRequestMapper.INSTANCE
                                .participationRequestToParticipationRequestDto(participationRequest));
            }
            participationRequestRepository.save(participationRequest);
        }
        return result;
    }

    @Override
    public List<EventFullDto> findEventsByConditions(List<Long> users, List<State> states, List<Long> categories,
                                                     LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                     int from, int size, HttpServletRequest request) {
        List<BooleanExpression> conditions = prepareConditions(users, states, rangeStart, rangeEnd);
        int pageNumber = from / size;
        PageRequest pageRequest = PageRequest.of(pageNumber, size);
        Optional<BooleanExpression> optionalCondition = conditions.stream().reduce(BooleanExpression::and);
        Page<Event> events;
        if (optionalCondition.isPresent()) {
            BooleanExpression finalCondition = optionalCondition.get();
            events = eventRepository.findAll(finalCondition, pageRequest);
        } else {
            events = eventRepository.findAll(pageRequest);
        }
        Optional<LocalDateTime> start = events.stream()
                .map(Event::getCreatedOn)
                .sorted()
                .findFirst();
        if (start.isEmpty()) {
            for (Event event : events) {
                event.setViews(0);
            }
        } else {
            LocalDateTime startLDT = start.get();
            List<String> urisList = new ArrayList<>();
            Map<String, Event> eventMap = events.stream()
                    .collect(Collectors.toMap(Event::getUri, Function.identity()));
            for (Event e : events) {
                urisList.add(e.getUri());
            }
            String uris = String.join("&uris=", urisList);
            List<ViewStatsDto> viewStatsDtos = statsClient.get(uris, startLDT, LocalDateTime.now());
            for (ViewStatsDto viewStatsDto : viewStatsDtos) {
                Event event = eventMap.get(viewStatsDto.getUri());
                event.setViews(viewStatsDto.getHits());
            }
        }
        for (Event event : events) {
            Integer confirmed = participationRequestRepository.countConfirmedRequests(event.getId());
            if (confirmed == null) {
                confirmed = 0;
            }
            event.setConfirmedRequests(confirmed);
            statsClient.saveHit(request, event.getUri(), LocalDateTime.now());
        }
        return events.stream()
                .map(EventMapper.INSTANCE::eventToEventFullDto)
                .collect(Collectors.toList());
    }

    private List<BooleanExpression> prepareConditions(List<Long> users, List<State> states,
                                                      LocalDateTime rangeStart, LocalDateTime rangeEnd) {
        QEvent event = QEvent.event;
        List<BooleanExpression> conditions = new ArrayList<>();
        if (users != null) {
            conditions.add(event.initiator.id.in(users));
        }
        if (states != null) {
            conditions.add(event.state.in(states));
        }
        if (rangeStart != null) {
            conditions.add(event.eventDate.after(rangeStart));
        }
        if (rangeEnd != null) {
            conditions.add(event.eventDate.before(rangeEnd));
        }
        return conditions;
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
}