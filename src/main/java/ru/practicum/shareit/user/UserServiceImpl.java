package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DataNotFoundException;
import ru.practicum.shareit.exception.StorageException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.entity.UserEntity;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.mapper.UserRepositoryMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserRepositoryMapper userRepositoryMapper;

    @Override
    public UserDto createUser(User user) {
        try {
            User createdUser = userRepositoryMapper.toUserFromEntity(
                    userRepository.save(
                            userRepositoryMapper.createToEntity(user)));
            return userMapper.toUserDto(createdUser);
        } catch (Exception exception) {
            throw new StorageException("Ошибка создания пользователя");
        }
    }

    @Override
    public UserDto updateUser(Long id, User user) {
        try {
            UserEntity stored = userRepository.findById(id).orElseThrow(StorageException::new);
            userRepositoryMapper.updateEntity(user,stored);
            User updatedUser = userRepositoryMapper.toUserFromEntity(userRepository.save(stored));
            return userMapper.toUserDto(updatedUser);
        } catch (StorageException e) {
            throw new DataNotFoundException("Ошибка обновления");
        }
    }

    @Override
    public UserDto getUser(Long id) throws DataNotFoundException {
        User recivedUser = userRepository.findById(id)
                .map(userRepositoryMapper::toUserFromEntity)
                .orElseThrow(DataNotFoundException::new);
        return userMapper.toUserDto(recivedUser);
    }

    @Override
    public void deleteUser(Long id) {
        UserEntity userEntity = userRepository.getReferenceById(id);
        userRepository.delete(userEntity);
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<UserEntity> allUsers = userRepository.findAll();
        List<UserDto> recivedUsersList = allUsers.stream()
                .map(userRepositoryMapper::toUserFromEntity)
                .map(userMapper::toUserDto)
                .collect(Collectors.toList());
        return recivedUsersList;
    }
}
