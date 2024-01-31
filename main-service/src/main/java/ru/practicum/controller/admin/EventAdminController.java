package ru.practicum.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.UpdateEventAdminRequest;
import ru.practicum.model.State;
import ru.practicum.service.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.config.Constants.ADMIN_CONTROLLER_PREFIX;


@RestController
@RequestMapping(path = ADMIN_CONTROLLER_PREFIX + "/events")
@RequiredArgsConstructor
@Slf4j
@Validated
public class EventAdminController {
    private final EventService eventService;

    @GetMapping()
    public List<EventFullDto> getEvents(
            @RequestParam(required = false) List<Long> users, @RequestParam(required = false) List<State> states,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
            @RequestParam(defaultValue = "0") @PositiveOrZero int from,
            @RequestParam(defaultValue = "10") @Positive int size,
            HttpServletRequest request) {
        log.info("Получен запрос {} — получение полной информации обо всех, " +
                "событиях, подходящих под переданные условия", request.getRequestURI());
        return eventService.findEventsByConditions(users, states, categories, rangeStart, rangeEnd, from, size, request);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto patchEventAdmin(@Valid @RequestBody UpdateEventAdminRequest updateEventAdminRequest,
                                        @PathVariable long eventId, HttpServletRequest request) {
        log.info("Получен запрос {} — редактировавние данных события " +
                "и его статуса (отклонение/публикация)", request.getRequestURI());
        return eventService.patchEventAdmin(eventId, updateEventAdminRequest);
    }
}
