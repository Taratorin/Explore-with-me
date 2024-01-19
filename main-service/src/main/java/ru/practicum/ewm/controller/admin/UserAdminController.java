package ru.practicum.ewm.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.UserDto;
import ru.practicum.ewm.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static ru.practicum.ewm.config.Constants.ADMIN_CONTROLLER_PREFIX;

@RestController
@RequestMapping(path = ADMIN_CONTROLLER_PREFIX + "/users")
@RequiredArgsConstructor
@Slf4j
public class UserAdminController {

    private final UserService userService;

    @PostMapping()
    public UserDto saveUser(@RequestBody UserDto userDto, HttpServletRequest request) {
        log.info("Получен запрос " + request.getRequestURI() + " — добавление пользователя");
        return userService.saveUser(userDto);
    }

    @GetMapping()
    public List<UserDto> findUsers(@RequestParam List<Integer> ids,
                                   @RequestParam(defaultValue = "0") int from,
                                   @RequestParam(defaultValue = "10") int size,
                                   HttpServletRequest request) {
        log.info("Получен запрос " + request.getRequestURI() + " — получение пользователей");
        return userService.findUsers(ids, from, size);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable long userId, HttpServletRequest request) {
        log.info("Получен запрос " + request.getRequestURI() + " — удаление пользователя");
        userService.deleteUser(userId);
    }

}
