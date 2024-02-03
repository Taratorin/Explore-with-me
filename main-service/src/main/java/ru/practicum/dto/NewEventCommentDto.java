package ru.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewEventCommentDto {
    private Long id;
    @Length(min = 3, max = 100)
    @NotBlank
    private String title;
    @Length(min = 20, max = 1000)
    @NotBlank
    private String text;
}
