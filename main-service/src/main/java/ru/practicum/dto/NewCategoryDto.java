package ru.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class NewCategoryDto {
    private Long id;
    @NotBlank
    @Length(max = 50)
    private String name;
}
