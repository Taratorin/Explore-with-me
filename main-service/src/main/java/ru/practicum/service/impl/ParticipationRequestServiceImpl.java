package ru.practicum.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.dto.ParticipationRequestDto;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.ParticipationRequestMapper;
import ru.practicum.model.*;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.ParticipationRequestRepository;
import ru.practicum.repository.UserRepository;
import ru.practicum.service.ParticipationRequestService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParticipationRequestServiceImpl implements ParticipationRequestService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final ParticipationRequestRepository participationRequestRepository;

    @Override
    public ParticipationRequestDto saveParticipationRequest(long userId, long eventId) {
        User requester = findUserById(userId);
        Event event = findEventById(eventId);
        Integer confirmedRequests = participationRequestRepository.countConfirmedRequests(eventId);
        event.setConfirmedRequests(Objects.requireNonNullElse(confirmedRequests, 0));
        requestValidation(event, requester);
        ParticipationRequest participationRequest = getParticipationRequest(requester, event);
        if (event.getParticipantLimit() == 0 || !event.getRequestModeration()) {
            participationRequest.setStatus(Status.CONFIRMED);
        } else {
            participationRequest.setStatus(Status.PENDING);
        }
        eventRepository.save(event);
        ParticipationRequest savedParticipationRequest = participationRequestRepository.save(participationRequest);
        return ParticipationRequestMapper.INSTANCE.participationRequestToParticipationRequestDto(savedParticipationRequest);
    }

    @Override
    public List<ParticipationRequestDto> getParticipationRequests(long userId) {
        User requester = findUserById(userId);
        List<ParticipationRequest> participationRequests = participationRequestRepository.findByRequester(requester);
        return participationRequests.stream()
                .map(ParticipationRequestMapper.INSTANCE::participationRequestToParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public ParticipationRequestDto cancelParticipationRequest(long userId, long requestId) {
        User user = findUserById(userId);
        ParticipationRequest participationRequest = findParticipationRequestById(requestId);
        if (participationRequest.getRequester() != user) {
            throw new BadRequestException("Отменять можно только свои запросы на участие");
        }
        participationRequest.setStatus(Status.CANCELED);
        ParticipationRequest canceledParticipationRequest = participationRequestRepository.save(participationRequest);
        return ParticipationRequestMapper.INSTANCE.participationRequestToParticipationRequestDto(canceledParticipationRequest);
    }

    private void requestValidation(Event event, User requester) {
        if (event.getInitiator() == requester) {
            throw new ConflictException("Инициатор события не может добавить запрос на участие в своём событии");
        }
        if (event.getParticipantLimit() > 0 && event.getConfirmedRequests() == event.getParticipantLimit()) {
            throw new ConflictException("Достигнут лимит участников.");
        }
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new ConflictException("Нельзя участвовать в неопубликованном событии");
        }
        if (findByRequesterAndEvent(requester, event).isPresent()) {
            throw new ConflictException("Нельзя добавить повторный запрос");
        }
    }

    private User findUserById(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id=" + userId + " не существует."));
    }

    private Event findEventById(long eventId) {
        return eventRepository.findEventById(eventId)
                .orElseThrow(() -> new NotFoundException("Мероприятие с id=" + eventId + " не найдено."));
    }

    private ParticipationRequest findParticipationRequestById(long requestId) {
        return participationRequestRepository.findParticipationRequestById(requestId)
                .orElseThrow(() -> new NotFoundException("Запрос с id=" + requestId + " не найден."));
    }

    private Optional<ParticipationRequest> findByRequesterAndEvent(User requester, Event event) {
        return participationRequestRepository.findByRequesterAndEvent(requester, event);
    }

    private ParticipationRequest getParticipationRequest(User requester, Event event) {
        return ParticipationRequest.builder()
                .created(LocalDateTime.now())
                .event(event)
                .requester(requester)
                .build();
    }
}
