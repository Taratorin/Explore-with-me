package ru.practicum.ewm.model;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Data
public class UpdateCompilationRequest {
    private Set<Event> events;
    private Boolean pinned;
    private String title;
}
