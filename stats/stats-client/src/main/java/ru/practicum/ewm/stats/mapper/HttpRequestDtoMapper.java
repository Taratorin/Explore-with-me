package ru.practicum.ewm.stats.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.dto.stats.EndpointHitDto;
import ru.practicum.ewm.dto.stats.HttpRequestDto;

@UtilityClass
public class HttpRequestDtoMapper {

    public EndpointHitDto toEndpointHitDto(HttpRequestDto httpRequestDto) {
        return EndpointHitDto.builder()
                .app(httpRequestDto.getApp())
                .uri(httpRequestDto.getRequest().getRequestURI())
                .ip(httpRequestDto.getRequest().getRemoteAddr())
                .timestamp(httpRequestDto.getTimestamp())
                .build();
    }
}
