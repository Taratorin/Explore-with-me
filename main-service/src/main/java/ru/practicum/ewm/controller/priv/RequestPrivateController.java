package ru.practicum.ewm.controller.priv;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.ParticipationRequestDto;
import ru.practicum.ewm.service.ParticipationRequestService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static ru.practicum.ewm.config.Constants.PRIVATE_CONTROLLER_PREFIX;

@RestController
@RequestMapping(path = PRIVATE_CONTROLLER_PREFIX + "/requests")
@RequiredArgsConstructor
@Slf4j
public class RequestPrivateController {
    private final ParticipationRequestService participationRequestService;

    @PostMapping("/eventId")
    public ParticipationRequestDto saveParticipationRequest(@PathVariable long userId, @PathVariable long eventId,
                                                            HttpServletRequest request) {
        log.info("Получен запрос " + request.getRequestURI() + " — добавление запроса от текущего пользователя" +
                " на участие в событии");
        return participationRequestService.saveParticipationRequest(userId, eventId);
    }

    @GetMapping()
    public List<ParticipationRequestDto> getParticipationRequests(@PathVariable long userId,
                                         HttpServletRequest request) {
        log.info("Получен запрос " + request.getRequestURI() + " — получение информации о заявках, " +
                "текущего пользователя на участие в чужих событиях");
        return participationRequestService.getParticipationRequests(userId);
    }

    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto cancelParticipationRequest(@PathVariable long userId, @PathVariable long requestId,
                                                            HttpServletRequest request) {
        log.info("Получен запрос " + request.getRequestURI() + " — отмена своего запроса на участие в событии");
        return participationRequestService.cancelParticipationRequest(userId, requestId);
    }

}
