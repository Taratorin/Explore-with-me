package ru.practicum.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.ComplaintDto;
import ru.practicum.dto.FullEventCommentDto;
import ru.practicum.dto.NewEventCommentDto;
import ru.practicum.dto.UpdateEventCommentDto;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.CommentMapper;
import ru.practicum.mapper.ComplaintMapper;
import ru.practicum.model.*;
import ru.practicum.repository.*;
import ru.practicum.service.CommentService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final ComplaintRepository complaintRepository;
    private final LikeRepository likeRepository;

    @PersistenceContext
    private EntityManager em;

    @Override
    public FullEventCommentDto saveComment(NewEventCommentDto commentDto, long userId, long eventId) {
        User author = findUserById(userId);
        Event event = findEventById(eventId);
        EventComment eventComment = getEventComment(commentDto, event, author);
        commentRepository.save(eventComment);
        return CommentMapper.INSTANCE.toFullEventCommentDto(eventComment);
    }

    @Override
    public ComplaintDto saveComplaint(ComplaintDto complaintDto, long userId, long commentId) {
        User complainer = findUserById(userId);
        EventComment comment = findCommentById(commentId);
        if (comment.getAuthor().equals(complainer)) {
            throw new ConflictException("Нельзя жаловаться на свои комментарии");
        }
        if (complaintRepository.existsByCommentAndComplainer(comment, complainer)) {
            throw new ConflictException("Нельзя повторно оставить жалобу на тот же комментарий");
        }
        Complaint complaint = ComplaintMapper.INSTANCE.toComplaint(complaintDto);
        complaint.setState(StateComplaint.PENDING);
        complaint.setComment(comment);
        complaint.setComplainer(complainer);
        return ComplaintMapper.INSTANCE.toComplaintDto(complaintRepository.save(complaint));
    }

    @Override
    public List<ComplaintDto> getAllComplaintsPending() {
        return complaintRepository.findAllByState(StateComplaint.PENDING).stream()
                .map(ComplaintMapper.INSTANCE::toComplaintDto)
                .collect(Collectors.toList());
    }

    @Override
    public ComplaintDto getComplaintsPendingById(long complaintId) {
        Complaint complaint = findComplaintById(complaintId);
        return ComplaintMapper.INSTANCE.toComplaintDto(complaint);
    }

    @Override
    public void reviewComplaint(long complaintId, boolean support) {
        Complaint complaint = findComplaintById(complaintId);
        if (support) {
            complaintRepository.deleteById(complaintId);
            commentRepository.deleteById(complaint.getComment().getId());
        } else {
            complaint.setState(StateComplaint.REFUSED);
        }
    }

    @Override
    public void deleteCommentById(long commentId) {
        findAndDeleteCommentById(commentId);
    }

    @Override
    public FullEventCommentDto getCommentById(long commentId) {
        return CommentMapper.INSTANCE.toFullEventCommentDto(findCommentById(commentId));
    }

    @Override
    @Transactional
    public void deleteComment(long userId, long commentId) {
        User user = findUserById(userId);
        EventComment comment = findCommentById(commentId);
        if (!comment.getAuthor().equals(user)) {
            throw new ConflictException("Можно удалять только свои комментарии");
        }
        complaintRepository.deleteAllByComment(comment);
        likeRepository.deleteAllByComment(comment);
        commentRepository.deleteById(commentId);
    }

    @Override
    public FullEventCommentDto patchComment(UpdateEventCommentDto updateEventCommentDto, long userId, long commentId) {
        User author = findUserById(userId);
        EventComment comment = findCommentById(commentId);
        if (!comment.getAuthor().equals(author)) {
            throw new ConflictException("Допускается редактировать только свои комментарии");
        }
        LocalDateTime tsEdition = comment.getTsEdition();
        if (tsEdition != null && tsEdition.isAfter(LocalDateTime.now().minusHours(1))) {
            throw new ConflictException("Допускается редактировать комментарий не чаще, чем один раз в час.");
        }
        CommentMapper.INSTANCE.updateComment(updateEventCommentDto, comment);
        comment.setIsEdited(true);
        comment.setTsEdition(LocalDateTime.now());
        return CommentMapper.INSTANCE.toFullEventCommentDto(commentRepository.save(comment));
    }

    @Override
    public FullEventCommentDto saveLike(long userId, long commentId, String likeString) {
        User user = findUserById(userId);
        EventComment comment = findCommentById(commentId);
        if (comment.getAuthor().equals(user)) {
            throw new ConflictException("Нельзя оценивать собственные комментарии.");
        }
        int likeInt = LikeStatus.getStatusInt(likeString);
        Like like = likeRepository.findByCommentAndUser(comment, user)
                .orElse(new Like(null, comment, user, likeInt));
        if (like.getLike() == likeInt && like.getId() != null) {
            likeRepository.delete(like);
        } else if (like.getLike() != likeInt && like.getId() != null) {
            like.setLike(likeInt);
            likeRepository.save(like);
        } else {
            likeRepository.save(like);
        }
        commentRepository.save(comment);
        em.clear();
        EventComment commentUpdated = commentRepository.findById(commentId).get();
        EventComment completed = completeComment(commentUpdated);
        return CommentMapper.INSTANCE.toFullEventCommentDto(completed);
    }

    @Override
    public List<FullEventCommentDto> getComments(long eventId, String text, int from, int size) {
        Event event = findEventById(eventId);
        int pageNumber = from / size;
        Pageable pageable = PageRequest.of(pageNumber, size);
        Page<EventComment> comments =
                commentRepository.findAllByEventAndTitleContainingIgnoreCaseOrTextContainingIgnoreCase(event, text, text, pageable);
        return comments.stream().map(CommentMapper.INSTANCE::toFullEventCommentDto).collect(Collectors.toList());
    }

    private void findAndDeleteCommentById(long commentId) {
        if (!commentRepository.existsById(commentId)) {
            throw new NotFoundException("Комментарий с id=" + commentId + " не найден.");
        } else {
            complaintRepository.deleteAllByCommentId(commentId);
            likeRepository.deleteAllByCommentId(commentId);
            commentRepository.deleteById(commentId);
        }
    }

    private EventComment getEventComment(NewEventCommentDto commentDto, Event event, User author) {
        return EventComment.builder()
                .event(event)
                .author(author)
                .title(commentDto.getTitle())
                .text(commentDto.getText())
                .ts(LocalDateTime.now())
                .likes(0L)
                .dislikes(0L)
                .isEdited(false)
                .build();
    }

    private User findUserById(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id=" + userId + " не найден."));
    }

    private Event findEventById(long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Мероприятие с id=" + eventId + " не найдено."));
    }

    private EventComment findCommentById(long commentId) {
        EventComment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Комментарий с id=" + commentId + " не найден."));
        return completeComment(comment);
    }

    private EventComment completeComment(EventComment comment) {
        CommentLikes commentLikes = likeRepository.getCommentLikesByComment(comment);
        if (commentLikes != null) {
            comment.setLikes(commentLikes.getLikes());
            comment.setDislikes(commentLikes.getDislikes());
            comment.setRating(commentLikes.getRating());
        }
        return comment;
    }

    private Complaint findComplaintById(long complaintId) {
        return complaintRepository.findById(complaintId)
                .orElseThrow(() -> new NotFoundException("Жалоба с id=" + complaintId + " не найдена."));
    }
}
