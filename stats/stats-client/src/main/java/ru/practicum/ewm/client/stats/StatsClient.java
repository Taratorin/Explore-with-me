package ru.practicum.ewm.client.stats;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public class StatsClient {
    protected final RestTemplate rest;

    // параметры через UriComponentsBuilder
    // String encodedPath = URLEncoder.encode(path);

    public StatsClient(RestTemplate rest) {
        this.rest = rest;
    }

    public <T> ResponseEntity<Object> post(String path, T body) {
        HttpEntity<T> requestEntity = new HttpEntity<>(body);
        ResponseEntity<Object> serverResponse;
        try {
            serverResponse = rest.exchange(path, HttpMethod.POST, requestEntity, Object.class);
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        }
        return prepareResponse(serverResponse);
    }

    public ResponseEntity<Object> get(String path, @Nullable Map<String, Object> parameters) {
        ResponseEntity<Object> serverResponse;
        try {
            if (parameters != null) {
                serverResponse = rest.exchange(path, HttpMethod.GET, null, Object.class, parameters);
            } else {
                serverResponse = rest.exchange(path, HttpMethod.GET, null, Object.class);
            }
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        }
        return prepareResponse(serverResponse);
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