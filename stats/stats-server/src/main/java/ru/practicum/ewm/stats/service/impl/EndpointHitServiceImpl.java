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
    public List<ViewStatsDto> getHit(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        if (!unique) {
            if (uris != null) {
                if (start != null) {
                    return endpointHitRepository.findByUriAllIp(uris, start, end);
                } else {
                    return endpointHitRepository.findByUriAllIpWithoutDates(uris);
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
                if (start != null) {
                    return endpointHitRepository.findByUriDistinctIp(uris, start, end);
                } else {
                    return endpointHitRepository.findByUriDistinctIpWithoutDates(uris);
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

//    @Override
//    public List<ViewStatsDto> getHit(LocalDateTime start, LocalDateTime end, String[] uris, boolean unique) {
//        QEndpointHit endpointHit = QEndpointHit.endpointHit;
//        List<BooleanExpression> conditions = new ArrayList<>();
//        if (start != null) {
//            conditions.add(endpointHit.ts.after(start));
//        }
//        if (end != null) {
//            conditions.add(endpointHit.ts.before(start));
//        }
//        if (uris != null) {
//            conditions.add(endpointHit.uri.in(uris));
//        }
//        if (unique) {
//            conditions.add(endpointHit.ip.);
//        }
//
//
//        Optional<BooleanExpression> optionalCondition = conditions.stream().reduce(BooleanExpression::and);
//        if (optionalCondition.isPresent()) {
//            BooleanExpression finalCondition = optionalCondition.get();
//            Iterable<EndpointHit> endpointHits = endpointHitRepository.findAll(finalCondition);
//            List<ViewStatsDto> ViewStatsDto = new ArrayList<>();
//
//        }
//    }
}
