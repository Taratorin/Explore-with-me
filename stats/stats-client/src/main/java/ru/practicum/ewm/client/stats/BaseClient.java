package ru.practicum.ewm.client.stats;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import ru.practicum.ewm.dto.stats.ViewStatsDto;

import java.util.List;
import java.util.Map;

public class BaseClient {
    protected final RestTemplate rest;

    public BaseClient(RestTemplate rest) {
        this.rest = rest;
    }

    public <T> void post(String path, T body) {
        HttpEntity<T> requestEntity = new HttpEntity<>(body);
        ResponseEntity<Object> serverResponse;
        try {
            serverResponse = rest.exchange(path, HttpMethod.POST, requestEntity, Object.class);
        } catch (HttpStatusCodeException e) {
            ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
            return;
        }
        prepareResponse(serverResponse);
    }

    public List<ViewStatsDto> get(String path, @Nullable Map<String, Object> parameters) {
        ParameterizedTypeReference<List<ViewStatsDto>> parameterizedTypeReference = new ParameterizedTypeReference<>() {
        };
        ResponseEntity<List<ViewStatsDto>> serverResponse;
        try {
            if (parameters != null) {
                serverResponse = rest.exchange(path, HttpMethod.GET, null, parameterizedTypeReference, parameters);
            } else {
                serverResponse = rest.exchange(path, HttpMethod.GET, null, parameterizedTypeReference);
            }
        } catch (HttpStatusCodeException e) {
            throw new RuntimeException(e.getStatusText());
        }
        return serverResponse.getBody();
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