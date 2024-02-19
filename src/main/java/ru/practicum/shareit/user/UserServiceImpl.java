package ru.practicum.shareit.user;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DataAlreadyExistException;
import ru.practicum.shareit.exception.DataNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final HashMap<Long, User> users = new HashMap<>();
    private Long generatedId = 0L;

    @Override
    public User createUser(User user) {
        user.setId(++generatedId);
        users.put(user.getId(), user);

        return user;
    }

    @Override
    public User updateUser(User user) {
        User existingUser = users.get(user.getId());

        if (user.getName() != null) {
            existingUser.setName(user.getName());
        }

        if (user.getEmail() != null) {
            existingUser.setEmail(user.getEmail());
        }

        users.put(user.getId(), existingUser);

        return existingUser;
    }

    @Override
    public User getUser(Long id) {
        User user = users.get(id);
        return user;
    }

    @Override
    public void deleteUser(Long id) {
      users.remove(id);
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }
}
