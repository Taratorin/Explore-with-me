package ru.practicum.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class CommentLikes {
    private Long commentId;
    private Long likes;
    private Long dislikes;
    private Long rating;
}
