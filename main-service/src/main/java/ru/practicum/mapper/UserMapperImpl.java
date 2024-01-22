package ru.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.UserDto;
import ru.practicum.model.User;

@UtilityClass
public class UserMapperImpl {

    public User toUser(UserDto userDto) {
        return User.builder()
                .id(userDto.getId())
                .name(userDto.getName())
                .email(userDto.getEmail())
                .build();
    }

    public UserDto toUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

}
