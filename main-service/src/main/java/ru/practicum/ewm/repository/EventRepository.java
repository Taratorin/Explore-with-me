package ru.practicum.ewm.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.User;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findAllByInitiator(User user, Pageable pageable);

    Optional<Event> findEventById(long eventId);

    Optional<Event> findEventByIdAndInitiator(long eventId, User initiator);
}

