package ru.practicum.shareit.user;

import ru.practicum.shareit.user.model.User;

import java.util.List;


public interface UserService {

    User createUser(User user);

    User updateUser(User user);

    User getUser(Long id);

    void deleteUser(Long id);

    List<User> getAllUsers();

}
