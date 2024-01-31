package ru.practicum.ewm.stats.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.stats.EndpointHitDto;
import ru.practicum.ewm.dto.stats.ViewStatsDto;
import ru.practicum.ewm.stats.service.EndpointHitService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Slf4j
public class StatServiceController {
    private final EndpointHitService endpointHitService;

    @PostMapping("/hit")
    public ResponseEntity<EndpointHitDto> saveHit(@RequestBody EndpointHitDto endpointHitDto) {
        log.info("Получен запрос POST /stats — добавление сведений о статистике");
        return ResponseEntity.status(201).body(endpointHitService.saveHit(endpointHitDto));
    }

    @GetMapping("/stats")
    public ResponseEntity<List<ViewStatsDto>> getHit(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
            @RequestParam(required = false) List<String> uris,
            @RequestParam(required = false, defaultValue = "false") boolean unique) {
        log.info("Получен запрос GET /stats — получение сведений о статистике");
        if (start.isAfter(end)) {
            return ResponseEntity.status(400).body(null);
        }
        return ResponseEntity.status(200).body(endpointHitService.getHit(start, end, uris, unique));
    }
}