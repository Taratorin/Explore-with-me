package ru.practicum.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import ru.practicum.model.Location;
import ru.practicum.model.StateAction;
import ru.practicum.valid.EventDateValidByAdminUpdate;
import ru.practicum.valid.EventDateValidByUserUpdate;

@Data
@EventDateValidByUserUpdate
public class UpdateEventUserRequest {
    @Length(min = 20, max = 2000)
    private String annotation;
    private Long category;
    @Length(min = 20, max = 7000)
    private String description;
    private String eventDate;
    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    private StateAction stateAction;
    @Length(min = 3, max = 120)
    private String title;
}
