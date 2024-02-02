package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.Complaint;
import ru.practicum.model.EventComment;
import ru.practicum.model.StateComplaint;
import ru.practicum.model.User;

import java.util.List;

public interface ComplaintRepository extends JpaRepository<Complaint, Long> {

    void deleteAllByCommentId(long commentId);

    boolean existsByCommentAndComplainer(EventComment comment, User complainer);

    List<Complaint> findAllByState(StateComplaint state);

    void deleteAllByComment(EventComment comment);
}