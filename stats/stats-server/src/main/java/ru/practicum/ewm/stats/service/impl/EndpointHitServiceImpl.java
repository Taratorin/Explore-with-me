package ru.practicum.ewm.stats.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.stats.EndpointHitDto;
import ru.practicum.ewm.dto.stats.ViewStatsDto;
import ru.practicum.ewm.stats.EndpointHitRepository;
import ru.practicum.ewm.stats.entity.EndpointHit;
import ru.practicum.ewm.stats.mapper.EndpointHitMapper;
import ru.practicum.ewm.stats.service.EndpointHitService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static ru.practicum.ewm.stats.mapper.Formatter.DATE_TIME_FORMATTER;

@Service
@RequiredArgsConstructor
public class EndpointHitServiceImpl implements EndpointHitService {
    private final EndpointHitRepository endpointHitRepository;

    @Override
    public void saveHit(EndpointHitDto endpointHitDto) {
        EndpointHit endpointHit = EndpointHitMapper.toEndpointHit(endpointHitDto);
        endpointHitRepository.save(endpointHit);
    }

    @Override
    public List<ViewStatsDto> getHit(String start, String end, String[] uris, boolean unique) {
        LocalDateTime startTime = LocalDateTime.parse(start, DATE_TIME_FORMATTER);
        LocalDateTime endTime = LocalDateTime.parse(end, DATE_TIME_FORMATTER);
        if (!unique) {
            if (uris != null) {
                List<String> urisList = new ArrayList<>(List.of(uris));
                return endpointHitRepository.findByUriAllIp(urisList, startTime, endTime);
            } else {
                return endpointHitRepository.findByEmptyUriAllIp(startTime, endTime);
            }
        } else {
            if (uris != null) {
                List<String> urisList = new ArrayList<>(List.of(uris));
                return endpointHitRepository.findByUriDistinctIp(urisList, startTime, endTime);
            } else {
                return endpointHitRepository.findByEmptyUriDistinctIp(startTime, endTime);
            }
        }
    }
}
