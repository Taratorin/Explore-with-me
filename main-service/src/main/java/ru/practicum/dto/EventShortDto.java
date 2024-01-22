package ru.practicum.dto;

import lombok.Data;

@Data
public class EventShortDto {
    private String annotation;
    private CategoryDto category;
    private int confirmedRequests;
    private String eventDate;
    private long id;
    private UserShortDto initiator;
    private boolean paid;
    private String title;
    private long views;
}
