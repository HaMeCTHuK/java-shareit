package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DataNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDto createUser(UserDto userDto) {
        userDto.setId(userRepository.getGenerateId());
        User user = userMapper.toUser(userDto);
        UserDto recivedUserDto = userMapper.toUserDto(userRepository.save(user));
        return recivedUserDto;
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        User user = userMapper.toUser(userDto);

        User existingUser = userRepository.get(user.getId());

        if (user.getName() != null) {
            existingUser.setName(user.getName());
        }

        if (user.getEmail() != null) {
            existingUser.setEmail(user.getEmail());
        }

        user = userRepository.save(existingUser);
        UserDto recivedUserDto = userMapper.toUserDto(user);

        return recivedUserDto;
    }

    @Override
    public UserDto getUser(Long id) {
        User user = userRepository.get(id);
        if (user == null) {
            throw new DataNotFoundException("Пользователь не найден");
        }
        UserDto userDto = userMapper.toUserDto(user);
        return userDto;
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.delete(id);
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> allUsers = userRepository.getAllUsers();
        List<UserDto> allUsersDto = allUsers.stream()
                .map(userMapper::toUserDto)
                .collect(Collectors.toList());
        return allUsersDto;
    }
}
