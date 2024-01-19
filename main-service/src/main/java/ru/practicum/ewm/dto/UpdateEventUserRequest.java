package ru.practicum.ewm.dto;

import lombok.Data;
import ru.practicum.ewm.model.Location;

@Data
public class UpdateEventUserRequest {
    private String annotation;
    private CategoryDto category;
    private String description;
    private String eventDate;
    private Location location;
    private boolean paid;
    private int participantLimit;
    private boolean requestModeration;
    private String stateAction;
    private String title;
}
