package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.DataAlreadyExistException;
import ru.practicum.shareit.exception.DataNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private final UserService userService;


    @PostMapping
    public UserDto createUser(@RequestBody @Valid UserDto userDto) {
        validateUser(userDto);
        log.info("Пытаемся добавить пользователя: {}", userDto);
        return userService.createUser(userDto);
    }

    @PatchMapping("/{userId}")
    public UserDto updateUser(@PathVariable Long userId,@RequestBody UserDto userDto) {

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
        return userService.updateUser(userDto);
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
