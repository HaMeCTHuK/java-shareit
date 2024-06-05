package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.exception.DataNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.entity.UserEntity;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.mapper.UserRepositoryMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserRepositoryMapper userRepositoryMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void createUser_Successful() {
        User user = new User();
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");

        UserEntity userEntity = new UserEntity();
        UserDto userDto = new UserDto();

        when(userRepositoryMapper.createToEntity(user)).thenReturn(userEntity);
        when(userRepository.save(userEntity)).thenReturn(userEntity);
        when(userRepositoryMapper.toUserFromEntity(userEntity)).thenReturn(user);
        when(userMapper.toUserDto(user)).thenReturn(userDto);

        UserDto createdUserDto = userService.createUser(user);

        assertNotNull(createdUserDto);
        assertEquals(userDto, createdUserDto);
        verify(userRepository, times(1)).save(userEntity);
    }

    @Test
    void updateUser_Successful() {
        Long userId = 1L;
        User user = new User();
        UserEntity storedUserEntity = new UserEntity();
        UserDto userDto = new UserDto();

        when(userRepository.findById(userId)).thenReturn(Optional.of(storedUserEntity));
        when(userRepository.save(storedUserEntity)).thenReturn(storedUserEntity);
        when(userRepositoryMapper.toUserFromEntity(storedUserEntity)).thenReturn(user);
        when(userMapper.toUserDto(user)).thenReturn(userDto);

        UserDto updatedUserDto = userService.updateUser(userId, user);

        assertNotNull(updatedUserDto);
        verify(userRepository, times(1)).save(storedUserEntity);
    }

    @Test
    void getUser_ExistingUser_Successful() {
        Long userId = 1L;
        User user = new User();
        UserEntity userEntity = new UserEntity();
        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
        when(userRepositoryMapper.toUserFromEntity(userEntity)).thenReturn(user);
        when(userMapper.toUserDto(user)).thenReturn(new UserDto());

        UserDto retrievedUserDto = userService.getUser(userId);

        assertNotNull(retrievedUserDto);
    }

    @Test
    void getUser_NonExistingUser_ThrowsDataNotFoundException() {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> userService.getUser(userId));
    }

    @Test
    void deleteUser_Successful() {
        Long userId = 1L;
        UserEntity userEntity = mock(UserEntity.class);
        when(userRepository.getReferenceById(userId)).thenReturn(userEntity);

        userService.deleteUser(userId);

        verify(userRepository, times(1)).delete(userEntity);
    }

    @Test
    void getAllUsers_Successful() {
        UserEntity userEntity = new UserEntity();
        when(userRepository.findAll()).thenReturn(Collections.singletonList(userEntity));
        when(userRepositoryMapper.toUserFromEntity(userEntity)).thenReturn(new User());
        when(userMapper.toUserDto(any(User.class))).thenReturn(new UserDto());

        assertEquals(1, userService.getAllUsers().size());
    }
}
