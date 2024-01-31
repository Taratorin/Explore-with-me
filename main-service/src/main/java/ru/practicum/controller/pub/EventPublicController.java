package ru.practicum.controller.pub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.EventShortDto;
import ru.practicum.model.SortType;
import ru.practicum.service.EventService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "/events")
@Slf4j
@RequiredArgsConstructor
public class EventPublicController {
    private final EventService eventService;

    @GetMapping()
    public List<EventShortDto> getEventsPublic(
            @RequestParam(required = false) String text, @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) boolean paid,
            @RequestParam(defaultValue = "false", required = false) boolean onlyAvailable,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
            @RequestParam(required = false) SortType sort, @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size, HttpServletRequest request) {
        log.info("Получен запрос {} — получение событий с возможностью фильтрации", request.getRequestURI());
        return eventService.getEventsPublic(text, categories, paid, onlyAvailable,
                rangeStart, rangeEnd, sort, from, size, request);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto getEventPublic(@PathVariable Long id, HttpServletRequest request) {
        log.info("Получен запрос {} — получение подробной информации об " +
                "опубликованном событии по его идентификатору", request.getRequestURI());
        return eventService.findEventPublic(id, request);
    }
}