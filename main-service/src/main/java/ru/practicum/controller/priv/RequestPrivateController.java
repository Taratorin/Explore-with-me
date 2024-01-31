package ru.practicum.controller.priv;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.ParticipationRequestDto;
import ru.practicum.service.ParticipationRequestService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static ru.practicum.config.Constants.PRIVATE_CONTROLLER_PREFIX;

@RestController
@RequestMapping(path = PRIVATE_CONTROLLER_PREFIX + "/requests")
@RequiredArgsConstructor
@Slf4j
public class RequestPrivateController {
    private final ParticipationRequestService participationRequestService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto saveParticipationRequest(
            @PathVariable long userId, @RequestParam long eventId,
            HttpServletRequest request) {
        log.info("Получен запрос {} — добавление запроса от текущего пользователя" +
                " на участие в событии", request.getRequestURI());
        return participationRequestService.saveParticipationRequest(userId, eventId);
    }

    @GetMapping()
    public List<ParticipationRequestDto> getParticipationRequests(@PathVariable long userId,
                                                                  HttpServletRequest request) {
        log.info("Получен запрос {} — получение информации о заявках, " +
                "текущего пользователя на участие в чужих событиях", request.getRequestURI());
        return participationRequestService.getParticipationRequests(userId);
    }

    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto cancelParticipationRequest(@PathVariable long userId, @PathVariable long requestId,
                                                              HttpServletRequest request) {
        log.info("Получен запрос {} — отмена своего запроса на участие в событии", request.getRequestURI());
        return participationRequestService.cancelParticipationRequest(userId, requestId);
    }
}