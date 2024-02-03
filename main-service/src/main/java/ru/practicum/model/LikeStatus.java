package ru.practicum.model;

import lombok.Getter;
import ru.practicum.exception.BadRequestException;

@Getter
public enum LikeStatus {
    LIKE(1),
    DISLIKE(-1);

    private final int status;

    LikeStatus(int status) {
        this.status = status;
    }

    public static int getStatusInt(String likeString) {
        for (LikeStatus val : LikeStatus.values()) {
            if (val.name().equalsIgnoreCase(likeString)) {
                return val.getStatus();
            }
        }
        throw new BadRequestException("Передано некорректное значение: " + likeString);
    }
}