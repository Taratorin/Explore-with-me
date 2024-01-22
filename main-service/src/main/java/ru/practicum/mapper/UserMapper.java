package ru.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.practicum.dto.UserDto;
import ru.practicum.model.User;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User userDtoToUser(UserDto userDto);

    UserDto userToUserDto(User user);
}
