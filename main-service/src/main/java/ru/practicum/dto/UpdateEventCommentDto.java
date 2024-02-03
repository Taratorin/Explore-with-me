package ru.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEventCommentDto {
    private Long id;
    @Length(min = 3, max = 100)
    private String title;
    @Length(min = 20, max = 1000)
    private String text;
}
