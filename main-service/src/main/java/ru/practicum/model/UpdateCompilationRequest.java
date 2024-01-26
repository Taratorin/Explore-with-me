package ru.practicum.model;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Data
public class UpdateCompilationRequest {
    private List<Long> events;
    private boolean pinned;
    @Length(min = 1, max = 50)
    private String title;
}
