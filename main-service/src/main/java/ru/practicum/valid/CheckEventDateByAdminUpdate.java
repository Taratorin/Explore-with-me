package ru.practicum.valid;


import ru.practicum.dto.UpdateEventAdminRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

import static ru.practicum.config.Constants.DATE_TIME_FORMATTER;

public class CheckEventDateByAdminUpdate implements ConstraintValidator<EventDateValidByAdminUpdate, UpdateEventAdminRequest> {
    private static final Long HOURS_VALID_AFTER_UPDATE = 2L;

    @Override
    public void initialize(EventDateValidByAdminUpdate constraintAnnotation) {
    }

    @Override
    public boolean isValid(UpdateEventAdminRequest updateEventAdminRequest, ConstraintValidatorContext constraintValidatorContext) {
        String eventDateString = updateEventAdminRequest.getEventDate();
        if (eventDateString == null) {
            return true;
        }
        LocalDateTime eventDate = LocalDateTime.parse(eventDateString, DATE_TIME_FORMATTER);
        return eventDate.isAfter(LocalDateTime.now().plusHours(HOURS_VALID_AFTER_UPDATE));
    }
}
