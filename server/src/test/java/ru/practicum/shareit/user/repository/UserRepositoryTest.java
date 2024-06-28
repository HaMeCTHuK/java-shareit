package ru.practicum.shareit.user.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.user.entity.UserEntity;


@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testCustomQuery() {
        UserEntity user = new UserEntity();
        user.setName("testUser");
        user.setEmail("test@example.com");

        UserEntity savedUser = userRepository.save(user);

        Assertions.assertNotNull(savedUser);
        Assertions.assertEquals("test@example.com", savedUser.getEmail());
        Assertions.assertEquals("testUser", savedUser.getName());
    }

}
