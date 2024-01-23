package ru.practicum.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.UserDto;
import ru.practicum.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@RestController
@RequestMapping(path = "/admin/users")
@RequiredArgsConstructor
@Slf4j
public class UserAdminController {

    private final UserService userService;

    @PostMapping()
    public ResponseEntity<UserDto> saveUser(@RequestBody UserDto userDto, HttpServletRequest request) {
        log.info("Получен запрос " + request.getRequestURI() + " — добавление пользователя");
        return ResponseEntity.status(201).body(userService.saveUser(userDto));
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
