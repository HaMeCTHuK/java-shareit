package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.DataAlreadyExistException;
import ru.practicum.shareit.exception.DataNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping
    public UserDto createUser(@RequestBody @Valid UserDto userDto) {
        User user = userMapper.toUser(userDto);
        validateUser(user);
        log.info("Пытаемся добавить пользователя: {}", user);
        UserDto recivedUserDto = userMapper.toUserDto(userService.createUser(user));
        return recivedUserDto;
    }

    @PatchMapping("/{userId}")
    public UserDto updateUser(@PathVariable Long userId,@RequestBody UserDto userDto) {
        userDto.setId(userId);
        User user = userMapper.toUser(userDto);

       // User existingUser = userService.getUser(userId);
        if (user == null) {
            throw new DataNotFoundException("Пользователь с ID " + userId + " не найден");
        }

        for (UserDto addedUser : getAllUsers()) {
            if (addedUser.getEmail().equals(user.getEmail()) && !addedUser.getId().equals(userId)) {
                throw new DataAlreadyExistException("Пользователь уже сужествует");
            }
        }

        log.info("Пытаемся обновить пользователя : {}", user);
        UserDto recivedUserDto = userMapper.toUserDto(user);
        return recivedUserDto;
    }

    @GetMapping("/{id}")
    public UserDto getUser(@RequestBody @PathVariable Long id) {
        log.info("Получаем объект по id: {}", id);
        UserDto userDto = userMapper.toUserDto(userService.getUser(id));
        return userDto;
    }

    @GetMapping
    @ResponseBody
    public List<UserDto> getAllUsers() {
        List<User> allUser = userService.getAllUsers();

        List<UserDto> allUsers = allUser.stream()
                .map(userMapper::toUserDto)
                .collect(Collectors.toList());

        log.info("Текущее количество пользователей: {}", allUsers.size());
        return allUsers;
    }

    @DeleteMapping("/{userId}")
    public void deleteUserById(@PathVariable Long userId) {
        if (getUser(userId) == null) {
            throw new DataNotFoundException("Пользователь с ID " + userId + " не найден");
        }
        log.info("Удаляем пользователя по ID: " + userId);
        userService.deleteUser(userId);
    }

    public void validateUser(User user) {
        if (user.getEmail() == null) {
            throw new ValidationException("Не корректные данные объекта");
        }

        for (UserDto addedUser : getAllUsers()) {
            if (addedUser.getEmail().equals(user.getEmail())) {
                throw new DataAlreadyExistException("Пользователь уже сужествует");
            }
        }
    }
}
