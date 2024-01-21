package ru.practicum.ewm.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import ru.practicum.ewm.model.Location;
import ru.practicum.ewm.model.StateAction;
import ru.practicum.ewm.valid.EventDateValidByUpdate;

import java.time.LocalDateTime;

@Data
@EventDateValidByUpdate
public class UpdateEventAdminRequest {
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
