package ru.practicum.ewm.stats.mapper;

import ru.practicum.ewm.dto.stats.EndpointHitDto;
import ru.practicum.ewm.stats.entity.EndpointHit;

import java.time.LocalDateTime;

import static ru.practicum.ewm.stats.mapper.Formatter.DATE_TIME_FORMATTER;

public class EndpointHitMapper {

    public static EndpointHit toEndpointHit(EndpointHitDto endpointHitDto) {
        LocalDateTime ts = LocalDateTime.parse(endpointHitDto.getTimestamp(), DATE_TIME_FORMATTER);
        return EndpointHit.builder()
                .app(endpointHitDto.getApp())
                .uri(endpointHitDto.getUri())
                .ip(endpointHitDto.getIp())
                .ts(ts)
                .build();
    }
}