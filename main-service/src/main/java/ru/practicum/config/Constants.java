package ru.practicum.config;

import lombok.experimental.UtilityClass;

import java.time.format.DateTimeFormatter;

@UtilityClass
public class Constants {
    public static final String ADMIN_CONTROLLER_PREFIX = "/admin";
    public static final String PRIVATE_CONTROLLER_PREFIX = "/users/{userId}";
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

}
