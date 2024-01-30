package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.model.Event;
import ru.practicum.model.ParticipationRequest;
import ru.practicum.model.User;

import java.util.List;
import java.util.Optional;

public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Long>,
        QuerydslPredicateExecutor<ParticipationRequest> {
    Optional<ParticipationRequest> findByRequesterAndEvent(User requester, Event event);

    List<ParticipationRequest> findByRequester(User requester);

    Optional<ParticipationRequest> findParticipationRequestById(long requestId);

    List<ParticipationRequest> findByEvent(Event event);

    List<ParticipationRequest> findByEventAndIdIn(Event event, List<Long> ids);

//    @Query("select new ru.practicum.dto.ConfirmedRequestsDto(count(?1)) " +
//            "from ParticipationRequest " +
//            "where status = 'CONFIRMED' " +
//            "group by ?1")
    @Query(value = "select count(?1) \n" +
            "from participation_requests pr \n" +
            "where pr.status = 'CONFIRMED'\n" +
            "and pr.event_id = ?1 \n" +
            "group by ?1",
    nativeQuery = true)
    Integer countConfirmedRequests(long eventId);
}