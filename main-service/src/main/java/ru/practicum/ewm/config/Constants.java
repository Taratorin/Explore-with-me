package ru.practicum.ewm.config;

import lombok.experimental.UtilityClass;

import java.time.format.DateTimeFormatter;

@UtilityClass
public class Constants {

//    public static final String X_SHARER_USER_ID = "X-Sharer-User-Id";
    public static final String ADMIN_CONTROLLER_PREFIX = "/admin";
    public static final String PRIVATE_CONTROLLER_PREFIX = "/users/{userId}";
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

}
