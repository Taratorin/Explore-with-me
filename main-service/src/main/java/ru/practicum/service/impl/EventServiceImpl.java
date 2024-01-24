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
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.EventMapper;
import ru.practicum.mapper.ParticipationRequestMapper;
import ru.practicum.model.*;
import ru.practicum.repository.*;
import ru.practicum.service.EventService;

import java.lang.reflect.Field;
import java.rmi.registry.LocateRegistry;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static ru.practicum.config.Constants.DATE_TIME_FORMATTER;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final ParticipationRequestRepository participationRequestRepository;
    private final CategoryRepository categoryRepository;
    private final LocationRepository locationRepository;

    @Override
    public EventFullDto saveNewEvent(NewEventDto newEventDto, long userId) {
        User user = findUserById(userId);
        Location location = locationRepository.save(newEventDto.getLocation());
        Event event = EventMapper.INSTANCE.newEventDtoToEvent(newEventDto);
        event.setConfirmedRequests(0);
        event.setLocation(location);
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
        if (event.getState().equals(State.CANCELED) || event.getState().equals(State.PENDING)) {
            updateEvent(updateEventUserRequest, event);
            Event savedEvent = eventRepository.save(event);
            return EventMapper.INSTANCE.eventToEventFullDto(savedEvent);
        }
        return EventMapper.INSTANCE.eventToEventFullDto(event);
    }

    @Override
    public EventFullDto patchEventAdmin(long eventId, UpdateEventAdminRequest updateEventAdminRequest) {
//        дата начала изменяемого события должна быть не ранее чем за час от даты публикации. (Ожидается код ошибки 409)
//        событие можно публиковать, только если оно в состоянии ожидания публикации (Ожидается код ошибки 409)
//        событие можно отклонить, только если оно еще не опубликовано (Ожидается код ошибки 409)
        Event event = findEventById(eventId);
        updateDestination(updateEventAdminRequest, event);
        if (StateAction.PUBLISH_EVENT.equals(updateEventAdminRequest.getStateAction()) &&
                event.getState().equals(State.PENDING)) {
            event.setState(State.PUBLISHED);
        }
        if (StateAction.REJECT_EVENT.equals(updateEventAdminRequest.getStateAction()) &&
                !event.getState().equals(State.PUBLISHED)) {
            event.setState(State.CANCELED);
        }
        Event savedEvent = eventRepository.save(event);
        return EventMapper.INSTANCE.eventToEventFullDto(savedEvent);
    }

    @Override
    public List<EventShortDto> getEventsPublic(String text, List<Long> categories, Boolean paid,
                                               Boolean onlyAvailable, LocalDateTime rangeStart,
                                               LocalDateTime rangeEnd, SortType sort, int from, int size) {
//        todo можно ли как-то уменьшить количество параметров метода?
//это публичный эндпоинт, соответственно в выдаче должны быть только опубликованные события
//текстовый поиск (по аннотации и подробному описанию) должен быть без учета регистра букв
//если в запросе не указан диапазон дат [rangeStart-rangeEnd], то нужно выгружать события, которые произойдут позже текущей даты и времени
//информация о каждом событии должна включать в себя количество просмотров и количество уже одобренных заявок на участие

        QEvent event = QEvent.event;
        QParticipationRequest request = QParticipationRequest.participationRequest;
        HibernateQuery<?> query = new HibernateQuery<Void>();
        query.from(event, request);
        List<BooleanExpression> conditions = new ArrayList<>();
        if (text != null) {
            conditions.add(event.annotation.containsIgnoreCase(text));
            conditions.add(event.description.containsIgnoreCase(text));
        }
        if (categories != null && !categories.isEmpty()) {
            conditions.add(event.category.id.in(categories));
        }
        if (paid != null) {
            conditions.add(event.paid.eq(paid));
        }
        if (onlyAvailable != null) {
//            todo как сформулировать условие
//            BooleanExpression condition = event.participantLimit.gt(event.);
//            conditions.add(condition);
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
        Iterable<Event> events = eventRepository.findAll(finalCondition, pageRequest);
        List<EventShortDto> eventShortDtos = new ArrayList<>();
        for (Event e : events) {
            eventShortDtos.add(EventMapper.INSTANCE.eventToEventShortDto(e));
        }
        return eventShortDtos;
    }

    @Override
    public EventFullDto getEventPublic(long id) {
        Event event = eventRepository.findByIdAndState(id, State.PUBLISHED)
                .orElseThrow(() -> new NotFoundException("Опубликованное мероприятие с id=" + id + " не найдено."));
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
        //        если для события лимит заявок равен 0 или отключена пре-модерация заявок, то подтверждение заявок не требуется
        //        нельзя подтвердить заявку, если уже достигнут лимит по заявкам на данное событие (Ожидается код ошибки 409)
        //        статус можно изменить только у заявок, находящихся в состоянии ожидания (Ожидается код ошибки 409)
        //        если при подтверждении данной заявки, лимит заявок для события исчерпан, то все неподтверждённые заявки необходимо отклонить
        User initiator = findUserById(userId);
        Event event = findEventById(eventId);
        if (event.getInitiator() != initiator) {
            throw new BadRequestException("Для изменения статуса заявок вы должны быть инициатором события.");
        }
        String statusString = eventRequestStatusUpdateRequest.getStatus();
        Status statusToSet = Status.valueOf(statusString);
        EventRequestStatusUpdateResult result = new EventRequestStatusUpdateResult();
        List<Long> ids = eventRequestStatusUpdateRequest.getRequestIds();
        List<ParticipationRequest> participationRequests = participationRequestRepository.findByEventAndIdIn(event, ids);
        for (ParticipationRequest participationRequest : participationRequests) {
            if (!Status.PENDING.equals(participationRequest.getStatus())) {
                throw new BadRequestException("Статус можно изменить только у заявок, находящихся в состоянии ожидания");
            }
            int confirmed = participationRequestRepository.findByEventAndStatus(event, Status.CONFIRMED).size();
            if (Status.CONFIRMED.equals(statusToSet)) {
                if (confirmed < event.getParticipantLimit()) {
                    participationRequest.setStatus(statusToSet);
                    result.getConfirmedRequests().add(ParticipationRequestMapper.INSTANCE.participationRequestToParticipationRequestDto(participationRequest));
                    participationRequestRepository.save(participationRequest);
                }
            } else {
                participationRequest.setStatus(Status.REJECTED);
                result.getRejectedRequests().add(ParticipationRequestMapper.INSTANCE.participationRequestToParticipationRequestDto(participationRequest));
                participationRequestRepository.save(participationRequest);
//                throw new BadRequestException("Уже достигнут лимит по заявкам на данное событие");
            }
        }
        return result;
    }

    @Override
    public List<EventFullDto> findEventsByConditions(List<Long> userIds, List<State> states, List<Long> categories,
                                                     LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                     int from, int size) {
        QEvent event = QEvent.event;
        List<BooleanExpression> conditions = new ArrayList<>();
        if (userIds != null) {
            conditions.add(event.initiator.id.in(userIds));
        }
        if (states != null) {
            conditions.add(event.state.in(states));
        }
        if (rangeStart != null) {
            conditions.add(event.eventDate.after(rangeStart));
        }
        if (rangeEnd != null) {
            conditions.add(event.eventDate.before(rangeStart));
        }
        int pageNumber = from / size;
        PageRequest pageRequest = PageRequest.of(pageNumber, size);
        Optional<BooleanExpression> optionalCondition = conditions.stream().reduce(BooleanExpression::and);
        if (optionalCondition.isPresent()) {
            BooleanExpression finalCondition = optionalCondition.get();
            Iterable<Event> events = eventRepository.findAll(finalCondition, pageRequest);
            List<EventFullDto> eventFullDtos = new ArrayList<>();
            for (Event e : events) {
                eventFullDtos.add(EventMapper.INSTANCE.eventToEventFullDto(e));
            }
            return eventFullDtos;
        } else {
            Page<Event> events = eventRepository.findAll(pageRequest);
            return events.stream()
                    .map(EventMapper.INSTANCE::eventToEventFullDto)
                    .collect(Collectors.toList());
        }
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
        if (!updateEventUserRequest.getCategoryId().equals(event.getCategory().getId())) {
            event.setId(updateEventUserRequest.getCategoryId());
        }
        if (!updateEventUserRequest.getDescription().equals(event.getDescription())) {
            event.setDescription(updateEventUserRequest.getDescription());
        }
        if (!updateEventUserRequest.getEventDate().equals(event.getEventDate())) {
            event.setEventDate(updateEventUserRequest.getEventDate());
        }
//        if (updateEventUserRequest.getLocation() != event.getLocation()) {
//            event.setLocation(updateEventUserRequest.getLocation());
//        }
        if (updateEventUserRequest.getPaid() && !event.getPaid()) {
            event.setPaid(updateEventUserRequest.getPaid());
        }
        if (updateEventUserRequest.getParticipantLimit() != event.getParticipantLimit()) {
            event.setParticipantLimit(updateEventUserRequest.getParticipantLimit());
        }
        if (updateEventUserRequest.getRequestModeration() && !event.getRequestModeration()) {
            event.setRequestModeration(updateEventUserRequest.getRequestModeration());
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

    private void updateDestination(Object source, Object destination) {
        Field[] fieldsSource = source.getClass().getDeclaredFields();
        Map<String, Object> mapFieldsValues = new HashMap<>(fieldsSource.length);
        Map<String, Object> mapFieldsTypes = new HashMap<>(fieldsSource.length);
        for (Field field : fieldsSource) {
            field.setAccessible(true);
            String name = field.getName();
            Class<?> type = field.getType();
            Object o;
            try {
                o = field.get(source);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            mapFieldsValues.put(name, o);
            mapFieldsTypes.put(name, type);
        }
        Field[] fieldsDestination = destination.getClass().getDeclaredFields();
        for (Field field : fieldsDestination) {
            field.setAccessible(true);
            String fieldDestinationName = field.getName();
            Object fieldValue = mapFieldsValues.get(fieldDestinationName);
            Object type = mapFieldsTypes.get(fieldDestinationName);
            if (fieldValue != null && field.getType() == type) {
                try {
                    field.set(destination, fieldValue);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                if (field.getName().equals("eventDate")) {
                    String eventDate = (String) mapFieldsValues.get("eventDate");
                    LocalDateTime localDateTime = LocalDateTime.parse(eventDate, DATE_TIME_FORMATTER);
                    try {
                        field.set(destination, localDateTime);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }
}