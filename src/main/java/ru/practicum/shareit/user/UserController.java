package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.DataAlreadyExistException;
import ru.practicum.shareit.exception.DataNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping
    public UserDto createUser(@RequestBody @Valid UserDto userDto) {
        //validateUser(userDto);
        log.info("Пытаемся добавить пользователя: {}", userDto);
        User user = userMapper.toUser(userDto);
        return userService.createUser(user);
    }

    @PatchMapping("/{userId}")
    public UserDto updateUser(@PathVariable @Min(1L) Long userId, @RequestBody UserDto userDto) {

        if (userDto == null) {
            throw new DataNotFoundException("Пользователь с ID " + userId + " не найден");
        }

        for (UserDto addedUser : userService.getAllUsers()) {
            if (addedUser.getEmail().equals(userDto.getEmail()) && !addedUser.getId().equals(userId)) {
                throw new DataAlreadyExistException("Пользователь уже сужествует");
            }
        }

        userDto.setId(userId);
        log.info("Пытаемся обновить пользователя : {}", userDto);
        return userService.updateUser(userId, userMapper.toUser(userDto));
    }

    @GetMapping("/{id}")
    public UserDto getUser(@RequestBody @PathVariable Long id) {
        log.info("Получаем объект по id: {}", id);
        return userService.getUser(id);
    }

    @GetMapping
    @ResponseBody
    public List<UserDto> getAllUsers() {
        List<UserDto> allUsers = userService.getAllUsers();
        log.info("Текущее количество пользователей: {}", allUsers.size());
        return allUsers;
    }

    @DeleteMapping("/{userId}")
    public void deleteUserById(@PathVariable Long userId) {
        if (userService.getUser(userId) == null) {
            throw new DataNotFoundException("Пользователь с ID " + userId + " не найден");
        }
        log.info("Удаляем пользователя по ID: " + userId);
        userService.deleteUser(userId);
    }

    public void validateUser(UserDto userDto) {
        if (userDto.getEmail() == null) {
            throw new ValidationException("Не корректные данные объекта");
        }

        for (UserDto addedUser : userService.getAllUsers()) {
            if (addedUser.getEmail().equals(userDto.getEmail())) {
                throw new DataAlreadyExistException("Пользователь уже сужествует");
            }
        }
    }
}
