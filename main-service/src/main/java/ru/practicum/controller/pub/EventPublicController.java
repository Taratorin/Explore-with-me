package ru.practicum.controller.pub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
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
@RequiredArgsConstructor
@Slf4j
public class EventPublicController {
    private final EventService eventService;

    @GetMapping()
    public List<EventShortDto> getEventsPublic(
            @RequestParam(required = false) String text, @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) boolean paid, @RequestParam(defaultValue = "false") boolean onlyAvailable,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
            @RequestParam(required = false) SortType sort, @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size, HttpServletRequest request) {
        log.info("Получен запрос " + request.getRequestURI() + " — получение событий " +
                "с возможностью фильтрации");
        return eventService.getEventsPublic(text, categories, paid, onlyAvailable,
                rangeStart, rangeEnd, sort, from, size, request);
    }

    @GetMapping("/{id}")
    public EventFullDto getEventPublic(@PathVariable Long id, HttpServletRequest request) {
        log.info("Получен запрос " + request.getRequestURI() + " — получение подробной информации об " +
                "опубликованном событии по его идентификатору");
        return eventService.findEventPublic(id, request);
    }
}
