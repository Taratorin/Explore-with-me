package ru.practicum.valid;


import ru.practicum.dto.NewEventDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class CheckEventDate implements ConstraintValidator<EventDateValid, NewEventDto> {
    private static final Long HOURS_VALID_AFTER_CREATION = 2L;

    @Override
    public void initialize(EventDateValid constraintAnnotation) {
    }

    @Override
    public boolean isValid(NewEventDto newEventDto, ConstraintValidatorContext constraintValidatorContext) {
        LocalDateTime eventDate = newEventDto.getEventDate();
        if (eventDate == null) {
            return false;
        }
        return eventDate.isAfter(LocalDateTime.now().plusHours(HOURS_VALID_AFTER_CREATION));
    }
}
