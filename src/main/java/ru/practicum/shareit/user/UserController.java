package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.DataAlreadyExistException;
import ru.practicum.shareit.exception.DataNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.model.User;

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

    private final UserService userService;

    @PostMapping
    public User createUser(@RequestBody @Valid User user) {
        validateUser(user);
        log.info("Пытаемся добавить пользователя: {}", user);
        return userService.createUser(user);
    }

    @PatchMapping("/{userId}")
    public User updateUser(@PathVariable Long userId,@RequestBody User user) {
        user.setId(userId);

        User existingUser = userService.getUser(userId);
        if (existingUser == null) {
            throw new DataNotFoundException("Пользователь с ID " + userId + " не найден");
        }

        for (User addedUser : getAllUsers()) {
            if (addedUser.getEmail().equals(user.getEmail()) && !addedUser.getId().equals(userId)) {
                throw new DataAlreadyExistException("Пользователь уже сужествует");
            }
        }

        log.info("Пытаемся обновить пользователя : {}", user);
        return userService.updateUser(user);
    }

    @GetMapping("/{id}")
    public User getUser(@RequestBody @PathVariable Long id) {
        log.info("Получаем объект по id: {}", id);
        return userService.getUser(id);
    }

    @GetMapping
    @ResponseBody
    public List<User> getAllUsers() {
        List<User> allUsers = userService.getAllUsers();
        log.info("Текущее количество пользователей: {}", allUsers.size());
        return  allUsers;
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

        for (User addedUser : getAllUsers()) {
            if (addedUser.getEmail().equals(user.getEmail())) {
                throw new DataAlreadyExistException("Пользователь уже сужествует");
            }
        }
    }
}
