package ru.practicum.ewm.stats.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.dto.stats.EndpointHitDto;
import ru.practicum.ewm.stats.entity.EndpointHit;

@UtilityClass
public class EndpointHitMapper {

    public static EndpointHit toEndpointHit(EndpointHitDto endpointHitDto) {
        return EndpointHit.builder()
                .app(endpointHitDto.getApp())
                .uri(endpointHitDto.getUri())
                .ip(endpointHitDto.getIp())
                .ts(endpointHitDto.getTimestamp())
                .build();
    }

}