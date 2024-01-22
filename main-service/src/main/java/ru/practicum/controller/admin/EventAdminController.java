package ru.practicum.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.UpdateEventAdminRequest;
import ru.practicum.model.State;
import ru.practicum.service.EventService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.config.Constants.ADMIN_CONTROLLER_PREFIX;


@RestController
@RequestMapping(path = ADMIN_CONTROLLER_PREFIX + "/events")
@RequiredArgsConstructor
@Slf4j
public class EventAdminController {
    private final EventService eventService;

    @GetMapping()
    public List<EventFullDto> getEvents(
            @RequestParam(required = false) List<Long> userIds, @RequestParam(required = false) List<State> states,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
            @RequestParam(defaultValue = "0") int from, @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        log.info("Получен запрос " + request.getRequestURI() + " — получение полной информации обо всех, " +
                "событиях, подходящих под переданные условия");
        return eventService.findEventsByConditions(userIds, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto patchEventAdmin(@RequestBody UpdateEventAdminRequest updateEventAdminRequest,
                                        @PathVariable long eventId, HttpServletRequest request) {
        log.info("Получен запрос " + request.getRequestURI() + " — редактировавние данных события " +
                "и его статуса (отклонение/публикация)");
        return eventService.patchEventAdmin(eventId, updateEventAdminRequest);
    }
}
