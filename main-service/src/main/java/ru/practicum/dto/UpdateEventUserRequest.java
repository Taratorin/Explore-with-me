package ru.practicum.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import ru.practicum.model.Location;
import ru.practicum.model.StateAction;
import ru.practicum.valid.EventDateValidByUpdate;

import java.time.LocalDateTime;

@Data
@EventDateValidByUpdate
public class UpdateEventUserRequest {
    @Length(min = 20, max = 2000)
    private String annotation;
    private Long category;
    @Length(min = 20, max = 7000)
    private String description;
    private LocalDateTime eventDate;
    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    private StateAction stateAction;
    private String title;
}