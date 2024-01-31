package ru.practicum.valid;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(ElementType.TYPE_USE)
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = CheckEventDate.class)
public @interface EventDateValid {
    String message() default "Event date must be not less 2 hours after creation";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}