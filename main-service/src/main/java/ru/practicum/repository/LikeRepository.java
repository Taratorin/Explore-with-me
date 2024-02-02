package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.EventComment;
import ru.practicum.model.Like;
import ru.practicum.model.User;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByCommentAndUser(EventComment comment, User user);

    void deleteAllByCommentId(long commentId);

    void deleteAllByComment(EventComment comment);
}
