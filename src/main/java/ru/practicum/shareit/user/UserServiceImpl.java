package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private Long generatedId = 0L;

    @Override
    public User createUser(User user) {


        userRepository.save(user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        User existingUser = userRepository.get(user.getId());

        if (user.getName() != null) {
            existingUser.setName(user.getName());
        }

        if (user.getEmail() != null) {
            existingUser.setEmail(user.getEmail());
        }

        userRepository.save(existingUser);

        return existingUser;
    }

    @Override
    public User getUser(Long id) {
        User user = userRepository.get(id);
        return user;
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.delete(id);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.getAllUsers();
    }
}
