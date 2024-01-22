package ru.practicum.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import ru.practicum.model.Location;
import ru.practicum.valid.EventDateValid;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@EventDateValid
public class NewEventDto {
    @NotBlank
    @Length(min = 20, max = 2000)
    private String annotation;
    @NotNull
    private Long category;
    @NotBlank
    @Length(min = 20, max = 7000)
    private String description;
    @NotBlank
    private LocalDateTime eventDate;
    @NotNull
    private Location location;
    private boolean paid = false;
    private int participantLimit = 0;
    private boolean requestModeration = true;
    @NotBlank
    @Length(min = 3, max = 120)
    private String title;
}