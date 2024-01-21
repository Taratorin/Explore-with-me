package ru.practicum.ewm.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.SortType;
import ru.practicum.ewm.model.State;
import ru.practicum.ewm.model.User;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long>, QuerydslPredicateExecutor<Event> {
    List<Event> findAllByInitiator(User user, Pageable pageable);

    Optional<Event> findEventById(long eventId);

    Optional<Event> findEventByIdAndInitiator(long eventId, User initiator);

    Optional<Event> findByIdAndState(long id, State state);
}