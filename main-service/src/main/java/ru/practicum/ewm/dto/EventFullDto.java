package ru.practicum.ewm.dto;

import lombok.Data;
import ru.practicum.ewm.model.Location;

@Data
public class EventFullDto {
    private String annotation;
    private CategoryDto category;
    private int confirmedRequests;
    private String createdOn;
    private String description;
    private String eventDate;
    private long id;
    private UserShortDto initiator;
    private Location location;
    private boolean paid;
    private int participantLimit;
    private String publishedOn;
    private boolean requestModeration;
    private String state;
    private String title;
    private long views;
}
