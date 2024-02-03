package ru.practicum.service;

import ru.practicum.dto.ComplaintDto;
import ru.practicum.dto.FullEventCommentDto;
import ru.practicum.dto.NewEventCommentDto;
import ru.practicum.dto.UpdateEventCommentDto;

import java.util.List;

public interface CommentService {
    FullEventCommentDto saveComment(NewEventCommentDto commentDto, long userId, long eventId);

    void deleteComment(long userId, long commentId);

    FullEventCommentDto patchComment(UpdateEventCommentDto commentDto, long userId, long commentId);

    FullEventCommentDto saveLike(long userId, long commentId, String like);

    List<FullEventCommentDto> getComments(long eventId, String text, int from, int size);

    ComplaintDto saveComplaint(ComplaintDto complaintDto, long userId, long commentId);

    List<ComplaintDto> getAllComplaintsPending();

    ComplaintDto getComplaintsPendingById(long complaintId);

    void reviewComplaint(long complaintId, boolean support);

    void deleteCommentById(long commentId);

    FullEventCommentDto getCommentById(long commentId);

}
