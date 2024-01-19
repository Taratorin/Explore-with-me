package ru.practicum.ewm.service;

import ru.practicum.ewm.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto saveUser(UserDto userDto);

    List<UserDto> findUsers(List<Integer> ids, int from, int size);

    void deleteUser(long userId);

}
