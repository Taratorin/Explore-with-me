package ru.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class LocationDto {
    @NotNull
    @Min(-90)
    @Max(90)
    private Float lat;
    @NotNull
    @Min(-180)
    @Max(180)
    private Float lon;
}
