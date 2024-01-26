package ru.practicum.valid;


import ru.practicum.dto.UpdateEventUserRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

import static ru.practicum.config.Constants.DATE_TIME_FORMATTER;

public class CheckEventDateByUserUpdate implements ConstraintValidator<EventDateValidByUserUpdate, UpdateEventUserRequest> {
    private static final Long HOURS_VALID_AFTER_UPDATE = 2L;

    @Override
    public void initialize(EventDateValidByUserUpdate constraintAnnotation) {
    }

    @Override
    public boolean isValid(UpdateEventUserRequest updateEventUserRequest, ConstraintValidatorContext constraintValidatorContext) {
        String eventDateString = updateEventUserRequest.getEventDate();
        if (eventDateString == null) {
            return true;
        }
        LocalDateTime eventDate = LocalDateTime.parse(eventDateString, DATE_TIME_FORMATTER);
        return eventDate.isAfter(LocalDateTime.now().plusHours(HOURS_VALID_AFTER_UPDATE));
    }
}
