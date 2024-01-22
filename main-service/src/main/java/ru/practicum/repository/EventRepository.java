package ru.practicum.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.model.Event;
import ru.practicum.model.State;
import ru.practicum.model.User;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long>, QuerydslPredicateExecutor<Event> {
    List<Event> findAllByInitiator(User user, Pageable pageable);

    Optional<Event> findEventById(long eventId);

    Optional<Event> findEventByIdAndInitiator(long eventId, User initiator);

    Optional<Event> findByIdAndState(long id, State state);
}