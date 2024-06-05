package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {

    UserDto createUser(User user);

    UserDto updateUser(Long id, User user);

    UserDto getUser(Long id);

    void deleteUser(Long id);

    List<UserDto> getAllUsers();

}
