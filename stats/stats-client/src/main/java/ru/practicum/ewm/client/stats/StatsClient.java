package ru.practicum.ewm.client.stats;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.practicum.ewm.dto.stats.EndpointHitDto;
import ru.practicum.ewm.dto.stats.HttpRequestDto;
import ru.practicum.ewm.dto.stats.mapper.HttpRequestDtoMapper;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Service
public class StatsClient extends BaseClient {
    private static final String API_PREFIX_POST = "/hit";
    private static final String API_PREFIX_GET = "/stats";

    public StatsClient(RestTemplate rest) {
        super(rest);
    }

    public void saveHit(HttpRequestDto httpRequestDto) {
        EndpointHitDto endpointHitDto = HttpRequestDtoMapper.toEndpointHitDto(httpRequestDto);
        super.post("${stats-server.url}" + API_PREFIX_POST, endpointHitDto);
    }

    public ResponseEntity<Object> get(HttpServletRequest request) {
        Map<String, String[]> parameters = request.getParameterMap();
        return super.get("${stats-server.url}" + API_PREFIX_GET, parameters);
    }

    private static ResponseEntity<Object> prepareResponse(ResponseEntity<Object> response) {
        if (response.getStatusCode().is2xxSuccessful()) {
            return response;
        }
        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(response.getStatusCode());
        if (response.hasBody()) {
            return responseBuilder.body(response.getBody());
        }
        return responseBuilder.build();
    }
}