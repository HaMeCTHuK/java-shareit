package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.entity.UserEntity;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.mapper.UserRepositoryMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserServiceTest {

    private UserRepository userRepository;
    private UserMapper userMapper;
    private UserRepositoryMapper userRepositoryMapper;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userMapper = Mappers.getMapper(UserMapper.class);
        userRepositoryMapper = Mappers.getMapper(UserRepositoryMapper.class);

        userService = new UserServiceImpl(userRepository,userMapper, userRepositoryMapper);
    }

    @Test
    void createUser() {
        var user = new User();
        user.setName("Test User");
        user.setEmail("test@example.com");

        var newUser = new User();
        newUser.setId(1L);
        newUser.setName("Test User");
        newUser.setEmail("test@example.com");

        var storedUser = new UserEntity();
        storedUser.setId(1L);
        storedUser.setName("User");
        storedUser.setEmail("test@test.com");


        var userDto = new UserDto();
        userDto.setId(1L);
        userDto.setName("Test2 User");
        userDto.setEmail("test@example.com");

        when(userRepository.save(any(UserEntity.class))).thenReturn(storedUser);

        var result = userService.createUser(user);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("User", result.getName());
        assertEquals("test@test.com", result.getEmail());
    }

    @Test
    void updateUser() {
        var user = new User();
        user.setName("Updated User");
        user.setEmail("test@test.com");

        var storedUser = new UserEntity();
        storedUser.setId(1L);
        storedUser.setName("Initial User");
        storedUser.setEmail("test@test.com");

        var userDto = new UserDto();
        userDto.setId(1L);
        userDto.setName("Test User");
        userDto.setEmail("test@test.com");

        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(storedUser));
        when(userRepository.save(any(UserEntity.class))).thenReturn(storedUser);

        var result = userService.updateUser(1L, user);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Updated User", result.getName());
        assertEquals("test@test.com", result.getEmail());
    }

    @Test
    void getUser() {
        var user = new User();
        user.setId(1L);
        user.setName("Test User");
        user.setEmail("test@test.com");

        var userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setName("Test User");
        userEntity.setEmail("test@test.com");

        var userDto = new UserDto();
        userDto.setId(1L);
        userDto.setName("Test User");
        userDto.setEmail("test@test.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity));

        var result = userService.getUser(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test User", result.getName());
        assertEquals("test@test.com", result.getEmail());
    }

    @Test
    void deleteUser() {
        var user = new UserEntity();
        user.setId(1L);
        user.setName("Test User");
        user.setEmail("test@test.com");

        when(userRepository.getReferenceById(1L)).thenReturn(user);

        assertDoesNotThrow(() -> userService.deleteUser(1L));
    }

    @Test
    void getAllUsers() {
        var user = new UserEntity();
        user.setId(1L);
        user.setName("test");
        user.setEmail("test@test.com");
        when(userRepository.findAll()).thenReturn(Collections.singletonList(user));

        var result = userService.getAllUsers();

        assertNotNull(result);
        assertEquals(user.getId(), result.get(0).getId());
    }
}