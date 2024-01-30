package ru.practicum.valid;


import ru.practicum.dto.UpdateEventRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class CheckEventDateByUpdate implements ConstraintValidator<EventDateValidByUpdate, UpdateEventRequest> {
    private static final Long HOURS_VALID_AFTER_UPDATE = 2L;

    @Override
    public void initialize(EventDateValidByUpdate constraintAnnotation) {
    }

    @Override
    public boolean isValid(UpdateEventRequest updateEventRequest, ConstraintValidatorContext constraintValidatorContext) {
        LocalDateTime eventDate = updateEventRequest.getEventDate();
        if (eventDate == null) {
            return true;
        }
        return eventDate.isAfter(LocalDateTime.now().plusHours(HOURS_VALID_AFTER_UPDATE));
    }
}
