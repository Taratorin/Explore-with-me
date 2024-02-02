package ru.practicum.controller.priv;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.*;
import ru.practicum.model.LikeStatus;
import ru.practicum.service.CommentService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

import static ru.practicum.config.Constants.PRIVATE_CONTROLLER_PREFIX;

@RestController
@RequestMapping(path = PRIVATE_CONTROLLER_PREFIX + "/comments")
@RequiredArgsConstructor
@Slf4j
@Validated
public class CommentPrivateController {
    private final CommentService commentService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public FullEventCommentDto saveComment(
            @Valid @RequestBody NewEventCommentDto commentDto,
            @PathVariable long userId, @RequestParam long eventId,
            HttpServletRequest request) {
        log.info("Получен запрос {} — добавление комментария к событию от текущего пользователя",
                request.getRequestURI());
        return commentService.saveComment(commentDto, userId, eventId);
    }

    @PostMapping("/{commentId}/complaints")
    @ResponseStatus(HttpStatus.CREATED)
    public ComplaintDto saveComplaint(@Valid @RequestBody ComplaintDto complaintDto,
                                      @PathVariable long userId, @PathVariable long commentId,
                                      HttpServletRequest request) {
        log.info("Получен запрос {} — добавление жалобы на комментарий от текущего пользователя",
                request.getRequestURI());
        return commentService.saveComplaint(complaintDto, userId, commentId);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable long userId, @PathVariable long commentId,
                              HttpServletRequest request) {
        log.info("Получен запрос {} — удаление комментария текущего пользователя к событию",
                request.getRequestURI());
        commentService.deleteComment(userId, commentId);
    }

    @PatchMapping("/{commentId}")
    public FullEventCommentDto patchComment(@Valid @RequestBody UpdateEventCommentDto updateEventCommentDto,
                                            @PathVariable long userId, @PathVariable long commentId,
                                            HttpServletRequest request) {
        log.info("Получен запрос {} — редактирование комментария текущего пользователя к событию",
                request.getRequestURI());
        return commentService.patchComment(updateEventCommentDto, userId, commentId);
    }

    @PostMapping("/{commentId}")
    @ResponseStatus(HttpStatus.CREATED)
    public FullEventCommentDto saveLike(@PathVariable long userId, @PathVariable long commentId, @RequestParam String like,
                         HttpServletRequest request) {
        log.info("Получен запрос {} — добавление/удаление лайка текущего пользователя к комментарию",
                request.getRequestURI());
        LikeStatus.isPresent(like);
        return commentService.saveLike(userId, commentId, like);
    }

    @GetMapping()
    public List<FullEventCommentDto> getComments(@RequestParam long eventId, @RequestParam @NotBlank String text,
                                                 @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                 @RequestParam(defaultValue = "10") @Positive int size,
                                                 HttpServletRequest request) {
        log.info("Получен запрос {} — поиск комментариев к событию с id = {}, содержащих указанный текст",
                request.getRequestURI(), eventId);
        return commentService.getComments(eventId, text, from, size);
    }
}