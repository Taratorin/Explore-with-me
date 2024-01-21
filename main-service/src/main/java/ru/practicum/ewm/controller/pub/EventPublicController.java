package ru.practicum.ewm.controller.pub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.client.stats.StatsClient;
import ru.practicum.ewm.dto.EventFullDto;
import ru.practicum.ewm.dto.EventShortDto;
import ru.practicum.ewm.model.SortType;
import ru.practicum.ewm.service.EventService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.ewm.config.Constants.APP_NAME;
import static ru.practicum.ewm.config.Constants.PRIVATE_CONTROLLER_PREFIX;

@RestController
@RequestMapping(path = "/events")
@RequiredArgsConstructor
@Slf4j
public class EventPublicController {
    private final EventService eventService;
    private final StatsClient statsClient;

    @GetMapping()
    public List<EventShortDto> getEventsPublic(
            @RequestParam(required = false) String text, @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) Boolean paid, @RequestParam(defaultValue = "false") Boolean onlyAvailable,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
            @RequestParam(required = false) SortType sort, @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size, HttpServletRequest request) {
        log.info("Получен запрос " + request.getRequestURI() + " — получение событий " +
                "с возможностью фильтрации");
        statsClient.saveHit(request, APP_NAME, LocalDateTime.now());
        return eventService.getEventsPublic(text, categories, paid, onlyAvailable,
                rangeStart, rangeEnd, sort, from, size);
    }

    @GetMapping("/{id}")
    public EventFullDto getEventPublic(@PathVariable long id, HttpServletRequest request) {
        log.info("Получен запрос " + request.getRequestURI() + " — получение подробной информации об " +
                "опубликованном событии по его идентификатору");
//        return eventService.getEventPublic(id);
        return null;
    }
}
