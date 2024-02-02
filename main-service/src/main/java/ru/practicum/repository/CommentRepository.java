package ru.practicum.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.Event;
import ru.practicum.model.EventComment;

public interface CommentRepository extends JpaRepository<EventComment, Long> {
    Page<EventComment> findAllByEventAndTitleContainingIgnoreCaseOrTextContainingIgnoreCase(Event event, String text, String text1, Pageable pageable);

}
