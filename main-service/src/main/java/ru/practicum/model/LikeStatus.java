package ru.practicum.model;

import ru.practicum.exception.BadRequestException;

public enum LikeStatus {
    LIKE, DISLIKE;

    public static boolean isPresent(String status) {
        try {
            Enum.valueOf(LikeStatus.class, status);
            return true;
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Передано некорректное значение: " + status);
        }
    }
}
