package ru.practicum.shareit.user;

import org.springframework.data.crossstore.ChangeSetPersister;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {

    UserDto createUser(UserDto userDto);

    UserDto updateUser(Long id, User user);

    UserDto getUser(Long id) throws ChangeSetPersister.NotFoundException;

    void deleteUser(Long id);

    List<UserDto> getAllUsers();

}
