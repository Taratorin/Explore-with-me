package ru.practicum.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import ru.practicum.model.Location;
import ru.practicum.model.StateAction;
import ru.practicum.valid.EventDateValidByAdminUpdate;

@Data
@EventDateValidByAdminUpdate
public class UpdateEventAdminRequest {
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
    private String title;
}
