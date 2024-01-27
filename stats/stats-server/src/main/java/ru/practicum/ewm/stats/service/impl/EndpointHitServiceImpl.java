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
    public List<ViewStatsDto> getHit(LocalDateTime start, LocalDateTime end, String[] uris, boolean unique) {
        if (!unique) {
            if (uris != null) {
                List<String> urisList = new ArrayList<>(List.of(uris));
                if (start != null) {
                    return endpointHitRepository.findByUriAllIp(urisList, start, end);
                } else {
                    return endpointHitRepository.findByUriAllIpWithoutDates(urisList);
                }
            } else {
                if (start != null) {
                    return endpointHitRepository.findByEmptyUriAllIp(start, end);
                } else {
                    return endpointHitRepository.findByEmptyUriAllIpWithoutDates();
                }
            }
        } else {
            if (uris != null) {
                List<String> urisList = new ArrayList<>(List.of(uris));
                if (start != null) {
                    return endpointHitRepository.findByUriDistinctIp(urisList, start, end);
                } else {
                    return endpointHitRepository.findByUriDistinctIpWithoutDates(urisList);
                }
            } else {
                if (start != null) {
                    return endpointHitRepository.findByEmptyUriDistinctIp(start, end);
                } else {
                    return endpointHitRepository.findByEmptyUriDistinctIpWithoutDates();
                }
            }
        }
    }
}
