package ru.practicum.shareit.user.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.entity.UserEntity;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.*;

public class UserRepositoryMapperTest {

    private UserRepositoryMapper userRepositoryMapper;

    @BeforeEach
    public void setUp() {
        userRepositoryMapper = Mappers.getMapper(UserRepositoryMapper.class);
    }

    @Test
    public void testCreateToUser() {
        UserDto userDto = new UserDto();
        userDto.setEmail("test@example.com");
        userDto.setName("Test User");

        User user = userRepositoryMapper.createToUser(userDto);

        assertNotNull(user);
        assertEquals(userDto.getEmail(), user.getEmail());
        assertEquals(userDto.getName(), user.getName());
        assertNull(user.getId());
    }

    @Test
    public void testCreateToEntity() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setName("Test User");

        UserEntity userEntity = userRepositoryMapper.createToEntity(user);

        assertNotNull(userEntity);
        assertEquals(user.getEmail(), userEntity.getEmail());
        assertEquals(user.getName(), userEntity.getName());
        assertNull(userEntity.getId());
    }

    @Test
    public void testToEntity() {
        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setName("Test User");

        UserEntity userEntity = userRepositoryMapper.toEntity(user);

        assertNotNull(userEntity);
        assertEquals(user.getId(), userEntity.getId());
        assertEquals(user.getEmail(), userEntity.getEmail());
        assertEquals(user.getName(), userEntity.getName());
    }

    @Test
    public void testToUserFromEntity() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setEmail("test@example.com");
        userEntity.setName("Test User");

        User user = userRepositoryMapper.toUserFromEntity(userEntity);

        assertNotNull(user);
        assertEquals(userEntity.getId(), user.getId());
        assertEquals(userEntity.getEmail(), user.getEmail());
        assertEquals(userEntity.getName(), user.getName());
    }

    @Test
    public void testUpdateEntity() {
        User user = new User();
        user.setEmail("updated@example.com");
        user.setName("Updated User");

        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setEmail("test@example.com");
        userEntity.setName("Test User");

        userRepositoryMapper.updateEntity(user, userEntity);

        assertEquals(userEntity.getId(), 1L);
        assertEquals(user.getEmail(), userEntity.getEmail());
        assertEquals(user.getName(), userEntity.getName());
    }
}
