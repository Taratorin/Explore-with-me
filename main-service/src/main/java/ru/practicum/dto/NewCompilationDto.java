package ru.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.util.Set;

@Data
@AllArgsConstructor
public class NewCompilationDto {
    private Boolean pinned;
    private Set<Long> events;
    @NotBlank
    @Length(min = 1, max = 50)
    private String title;
}
