package ru.practicum.ewm.valid;

import ru.practicum.ewm.dto.UpdateEventAdminRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class CheckEventDateByUpdate implements ConstraintValidator<EventDateValidByUpdate, UpdateEventAdminRequest> {
    private static final Long HOURS_VALID_AFTER_UPDATE = 2L;

    @Override
    public void initialize(EventDateValidByUpdate constraintAnnotation) {
    }

    @Override
    public boolean isValid(UpdateEventAdminRequest updateEventAdminRequest, ConstraintValidatorContext constraintValidatorContext) {
        LocalDateTime eventDate = updateEventAdminRequest.getEventDate();
        if (eventDate == null) {
            return true;
        }
        return eventDate.isAfter(LocalDateTime.now().plusHours(HOURS_VALID_AFTER_UPDATE));
    }
}
