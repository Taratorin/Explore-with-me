package ru.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FullEventCommentDto {
    private long id;
    private String title;
    private String text;
    private UserShortDto author;
    private LocalDateTime ts;
    private long likes;
    private long dislikes;
    private long rating;
    private Boolean isEdited;
    private LocalDateTime tsEdition;
}
