package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@Component
public class UserRepository {

    private final HashMap<Long, User> users = new HashMap<>();

    public User save(User user) {
        users.put(user.getId(), user);
       return user;
    }

    public User get(Long userId) {
        return users.get(userId);
    }

    public void delete(Long userId) {
        users.remove(userId);
    }

    public List<User> getAllUsers() {
      return new ArrayList<>(users.values());
    }
}
