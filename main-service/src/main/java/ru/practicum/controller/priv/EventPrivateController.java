package ru.practicum.controller.priv;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.*;
import ru.practicum.service.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

import static ru.practicum.config.Constants.PRIVATE_CONTROLLER_PREFIX;


@RestController
@RequestMapping(path = PRIVATE_CONTROLLER_PREFIX + "/events")
@RequiredArgsConstructor
@Slf4j
public class EventPrivateController {
    private final EventService eventService;

    @PostMapping()
//    todo delete DONE
    public ResponseEntity<EventFullDto> saveNewEvent(@Valid @RequestBody NewEventDto newEventDto,
                                     @PathVariable long userId,
                                     HttpServletRequest request) {
        log.info("Получен запрос " + request.getRequestURI() + " — добавление нового события");
        return ResponseEntity.status(201).body(eventService.saveNewEvent(newEventDto, userId));
    }

    @GetMapping()
    public List<EventShortDto> getEvents(@PathVariable long userId,
                                         @RequestParam(defaultValue = "0") int from,
                                         @RequestParam(defaultValue = "10") int size,
                                         HttpServletRequest request) {
        log.info("Получен запрос " + request.getRequestURI() + " — получение событий, " +
                "добавленных текущим пользователем");
        return eventService.getEvents(userId, from, size);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEvent(@PathVariable long userId, @PathVariable long eventId,
                                 HttpServletRequest request) {
        log.info("Получен запрос " + request.getRequestURI() + " — получение полной информации о событии, " +
                "добавленном текущим пользователем");
        return eventService.getEvent(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto patchEvent(@RequestBody UpdateEventUserRequest updateEventUserRequest,
                                   @PathVariable long userId, @PathVariable long eventId,
                                   HttpServletRequest request) {
        log.info("Получен запрос " + request.getRequestURI() + " — изменение события, " +
                "добавленного текущим пользователем");
        return eventService.patchEventUser(userId, eventId, updateEventUserRequest);
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> getEventRequest(@PathVariable long userId, @PathVariable long eventId,
                                                         HttpServletRequest request) {
        log.info("Получен запрос " + request.getRequestURI() + " — получение информации о запросах на участие, " +
                "в событии текущего пользователя");
        return eventService.getEventRequests(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    public EventRequestStatusUpdateResult patchEventRequests(
            @RequestBody EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest,
            @PathVariable long userId, @PathVariable long eventId,
            HttpServletRequest request) {
        log.info("Получен запрос " + request.getRequestURI() + " — изменение статуса заявок на участие, " +
                "в событии текущего пользователя");
        return eventService.patchEventRequests(userId, eventId, eventRequestStatusUpdateRequest);
    }

}
