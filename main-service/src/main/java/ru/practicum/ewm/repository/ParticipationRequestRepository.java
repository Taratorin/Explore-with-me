package ru.practicum.ewm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.ParticipationRequest;
import ru.practicum.ewm.model.State;
import ru.practicum.ewm.model.User;

import java.util.List;
import java.util.Optional;

public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Long> {
    Optional<ParticipationRequest> findByRequesterAndEvent(User requester, Event event);

    List<ParticipationRequest> findByEventAndStatus(Event event, State state);

    List<ParticipationRequest> findByRequester(User requester);

    Optional<ParticipationRequest> findParticipationRequestById(long requestId);

    List<ParticipationRequest> findByEvent(Event event);

    List<ParticipationRequest> findByEventAndIdIn(Event event, List<Long> ids);

}
