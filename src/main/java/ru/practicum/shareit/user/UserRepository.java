package ru.practicum.shareit.user;

import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.List;


public class UserRepository {

    private final HashMap<Long, User> users = new HashMap<>();

    public User save(User user) {

       return null;
    }

    public User get(Long userId) {
        return null;
    }

    public void delete(Long userId) {

    }

    public List<User> getAllUsers() {
      return null;
    }
}
