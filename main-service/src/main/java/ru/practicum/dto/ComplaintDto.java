package ru.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComplaintDto {
    private Long id;
    @Length(min = 10, max = 200)
    @NotBlank
    private String text;
    private FullEventCommentDto comment;
}
