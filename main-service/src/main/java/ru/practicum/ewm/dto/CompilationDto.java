package ru.practicum.ewm.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class CompilationDto {
    @NotNull
    private Long id;
    private List<EventShortDto> events;
    private Boolean pinned;
    @NotBlank
    private String title;
}
