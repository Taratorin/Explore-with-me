package ru.practicum.ewm.stats.service;

import ru.practicum.ewm.dto.stats.EndpointHitDto;
import ru.practicum.ewm.dto.stats.ViewStatsDto;

import java.util.List;

public interface EndpointHitService {
    void saveHit(EndpointHitDto endpointHitDto);

    List<ViewStatsDto> getHit(String start, String end, String[] uris, boolean unique);
}
