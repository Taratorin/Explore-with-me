package ru.practicum.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.ComplaintDto;
import ru.practicum.dto.FullEventCommentDto;
import ru.practicum.service.CommentService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static ru.practicum.config.Constants.ADMIN_CONTROLLER_PREFIX;

@RestController
@RequestMapping(ADMIN_CONTROLLER_PREFIX + "/comments")
@RequiredArgsConstructor
@Slf4j
public class CommentAdminController {
    private final CommentService commentService;

    @GetMapping("/complaints")
    public List<ComplaintDto> getAllComplaintsPending(HttpServletRequest request) {
        log.info("Получен запрос {} — найти все жалобы, ожидающие рассмотрения", request.getRequestURI());
        return commentService.getAllComplaintsPending();
    }

    @GetMapping("/complaints/{complaintId}")
    public ComplaintDto getComplaintsPendingById(@PathVariable long complaintId, HttpServletRequest request) {
        log.info("Получен запрос {} — найти жалобу по id = {}", request.getRequestURI(), complaintId);
        return commentService.getComplaintsPendingById(complaintId);
    }

    @GetMapping("/{commentId}")
    public FullEventCommentDto getCommentById(@PathVariable long commentId, HttpServletRequest request) {
        log.info("Получен запрос {} — найти комментариий по id = {}", request.getRequestURI(), commentId);
        return commentService.getCommentById(commentId);
    }

    @PatchMapping("/complaints/{complaintId}")
    public void reviewComplaint(@PathVariable long complaintId, @RequestParam boolean support,
                                HttpServletRequest request) {
        log.info("Получен запрос {} — рассмотреть жалобу на комментарий", request.getRequestURI());
        commentService.reviewComplaint(complaintId, support);
    }

    @DeleteMapping("/{commentId}")
    public void deleteCommentById(@PathVariable long commentId, HttpServletRequest request) {
        log.info("Получен запрос {} — удалить комментарий по id = {}", request.getRequestURI(), commentId);
        commentService.deleteCommentById(commentId);
    }
}