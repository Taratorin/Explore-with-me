package ru.practicum.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.dto.UserDto;
import ru.practicum.exception.ConflictException;
import ru.practicum.mapper.UserMapper;
import ru.practicum.model.User;
import ru.practicum.repository.UserRepository;
import ru.practicum.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserDto saveUser(UserDto userDto) {
        checkUniqueName(userDto);
        User user = UserMapper.INSTANCE.userDtoToUser(userDto);
        User savedUser = userRepository.save(user);
        return UserMapper.INSTANCE.userToUserDto(savedUser);
    }

    @Override
    public List<UserDto> findUsers(List<Long> ids, int from, int size) {
        int pageNumber = from / size;
        Pageable pageable = PageRequest.of(pageNumber, size, Sort.by("id"));
        List<User> users = new ArrayList<>();
        if (ids != null) {
            users.addAll(userRepository.findByIdInOrderById(ids, pageable));
        } else {
            Page<User> usersPage = userRepository.findAll(pageable);
            users.addAll(usersPage.toList());
        }
        return users.stream()
                .map(UserMapper.INSTANCE::userToUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteUser(long userId) {
        userRepository.deleteById(userId);
    }

    private void checkUniqueName(UserDto userDto) {
        if (userRepository.existsByName(userDto.getName())) {
            throw new ConflictException("Integrity constraint has been violated.");
        }
    }
}
