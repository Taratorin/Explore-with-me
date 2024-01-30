package ru.practicum.dto;

import lombok.Data;

import java.util.Set;

@Data
public class EventRequestStatusUpdateRequest {
    private Set<Long> requestIds;
    private Status status;

    public enum Status {
        CONFIRMED,
        REJECTED
    }
}
