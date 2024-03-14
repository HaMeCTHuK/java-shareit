package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DataNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.entity.UserEntity;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

/*    @Override
    public UserDto createUser(UserDto userDto) {
        userDto.setId(++generatedId);
        User user = userMapper.toUser(userDto);
        UserEntity userEntity = userMapper.toEntity(user);

        User createdUser = userMapper.toUserFromEntity(userRepository.save(userEntity));

        return userMapper.toUserDto(createdUser);
    }*/

    @Override
    public UserDto createUser(UserDto userdto) {
        try {
            User user = userMapper.createToUser(userdto);
            User createdUser = userMapper.toUserFromEntity(userRepository.save(userMapper.createToEntity(user)));
            return userMapper.toUserDto(createdUser);
        } catch (Exception exception) {
            throw new ValidationException("тут надо придумать с исключением");
        }
    }




/*    @Override
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
    }*/

    @Override
    public UserDto updateUser(Long id, User user) {
        try {
            UserEntity stored = userRepository.findById(id).orElseThrow(ChangeSetPersister.NotFoundException::new);
            userMapper.updateEntity(user,stored);
            User updatedUser = userMapper.toUserFromEntity(userRepository.save(stored));
            return userMapper.toUserDto(updatedUser);
        } catch (ChangeSetPersister.NotFoundException e) {
            throw new DataNotFoundException("Не найдено");
        }
    }

 /*   @Override
    public UserDto getUser(Long id) {
        User user = userRepository.get(id);
        if (user == null) {
            throw new DataNotFoundException("Пользователь не найден");
        }
        UserDto userDto = userMapper.toUserDto(user);
        return userDto;
    }*/

    @Override
    public UserDto getUser(Long id) throws ChangeSetPersister.NotFoundException {
        User recivedUser = userRepository.findById(id)
                .map(userMapper::toUserFromEntity)
                .orElseThrow(ChangeSetPersister.NotFoundException::new);
        return userMapper.toUserDto(recivedUser);
    }

/*    @Override
    public void deleteUser(Long id) {
        userRepository.delete(id);
    }*/

    @Override
    public void deleteUser(Long id) {
        UserEntity userEntity = userRepository.getReferenceById(id);
        userRepository.delete(userEntity);
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<UserEntity> allUsers = userRepository.findAll();
        List<UserDto> recivedUsersList = allUsers.stream()
                .map(userMapper::toUserFromEntity)
                .map(userMapper::toUserDto)
                .collect(Collectors.toList());
        return recivedUsersList;
    }
}
