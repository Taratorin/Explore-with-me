package ru.practicum.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.UserDto;
import ru.practicum.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static ru.practicum.config.Constants.ADMIN_CONTROLLER_PREFIX;


@RestController
@RequestMapping(path = ADMIN_CONTROLLER_PREFIX + "/users")
@RequiredArgsConstructor
@Slf4j
public class UserAdminController {
    private final UserService userService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto saveUser(@Validated @RequestBody UserDto userDto, HttpServletRequest request) {
        log.info("Получен запрос {} — добавление пользователя", request.getRequestURI());
        return userService.saveUser(userDto);
    }

    @GetMapping()
    public List<UserDto> findUsers(@RequestParam(required = false) List<Long> ids,
                                   @RequestParam(defaultValue = "0") int from,
                                   @RequestParam(defaultValue = "10") int size,
                                   HttpServletRequest request) {
        log.info("Получен запрос {} — получение пользователей", request.getRequestURI());
        return userService.findUsers(ids, from, size);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable long userId, HttpServletRequest request) {
        log.info("Получен запрос {} — удаление пользователя", request.getRequestURI());
        userService.deleteUser(userId);
    }
}