package ru.practicum.shareit.user.entity;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserEntityTest {

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void whenSaveUser_thenUserIsSaved() {
        UserEntity user = new UserEntity();
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        UserEntity savedUser = entityManager.persistFlushFind(user);

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getName()).isEqualTo("John Doe");
        assertThat(savedUser.getEmail()).isEqualTo("john.doe@example.com");
    }

    @Test
    public void whenFindUserById_thenUserIsFound() {
        UserEntity user = new UserEntity();
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        UserEntity savedUser = entityManager.persistFlushFind(user);

        UserEntity foundUser = entityManager.find(UserEntity.class, savedUser.getId());

        assertThat(foundUser).isEqualTo(savedUser);
    }

    @Test
    public void whenUpdateUser_thenUserIsUpdated() {
        UserEntity user = new UserEntity();
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        UserEntity savedUser = entityManager.persistFlushFind(user);

        savedUser.setName("Jane Doe");
        entityManager.persistAndFlush(savedUser);

        UserEntity updatedUser = entityManager.find(UserEntity.class, savedUser.getId());

        assertThat(updatedUser.getName()).isEqualTo("Jane Doe");
    }

    @Test
    public void whenDeleteUser_thenUserIsDeleted() {
        UserEntity user = new UserEntity();
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        UserEntity savedUser = entityManager.persistFlushFind(user);

        entityManager.remove(savedUser);
        entityManager.flush();

        UserEntity deletedUser = entityManager.find(UserEntity.class, savedUser.getId());

        assertThat(deletedUser).isNull();
    }
}
