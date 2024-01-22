package ru.practicum.model;

import lombok.Data;

import java.util.Set;

@Data
public class UpdateCompilationRequest {
    private Set<Event> events;
    private Boolean pinned;
    private String title;
}
