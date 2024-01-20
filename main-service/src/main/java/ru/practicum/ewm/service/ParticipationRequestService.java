package ru.practicum.ewm.service;

import ru.practicum.ewm.dto.ParticipationRequestDto;

import java.util.List;

public interface ParticipationRequestService {
    ParticipationRequestDto saveParticipationRequest(long userId, long eventId);

    List<ParticipationRequestDto> getParticipationRequests(long userId);

    ParticipationRequestDto cancelParticipationRequest(long userId, long requestId);
}
