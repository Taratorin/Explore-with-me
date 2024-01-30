package ru.practicum.ewm.client.stats;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.ewm.dto.stats.EndpointHitDto;
import ru.practicum.ewm.dto.stats.ViewStatsDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StatsClient extends BaseClient {
    private static final String API_PREFIX_POST = "/hit";
    private static final String API_PREFIX_GET = "/stats";
    @Value("${app.name}")
    private String appName;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public StatsClient(@Value("${stats-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public void saveHit(HttpServletRequest request, LocalDateTime ts) {
        EndpointHitDto endpointHitDto = EndpointHitDto.builder()
                .ip(request.getRemoteAddr())
                .uri(request.getRequestURI())
                .app(appName)
                .timestamp(ts)
                .build();
        post(API_PREFIX_POST, endpointHitDto);
    }

    public void saveHit(HttpServletRequest request, String uri, LocalDateTime ts) {
        EndpointHitDto endpointHitDto = EndpointHitDto.builder()
                .ip(request.getRemoteAddr())
                .uri(uri)
                .app(appName)
                .timestamp(ts)
                .build();
        post(API_PREFIX_POST, endpointHitDto);
    }

    public List<ViewStatsDto> get(String uris, LocalDateTime start, LocalDateTime end) {
        Map<String, Object> parameters = getParameters(uris, start, end);
        return get(API_PREFIX_GET + "?start={start}&end={end}&uris={uris}&unique={unique}", parameters);
    }

    private Map<String, Object> getParameters(String uris, LocalDateTime startLDT, LocalDateTime endLDT) {
        List<String> urisList = new ArrayList<>(List.of(uris.split("&uris=")));
        Map<String, Object> parameters = new HashMap<>();
        for (String s : urisList) {
            parameters.put("uris", s);
        }
        String start = startLDT.format(formatter);
        String end = endLDT.format(formatter);
        parameters.put("start", start);
        parameters.put("end", end);
        parameters.put("unique", "true");
        return parameters;
    }
}