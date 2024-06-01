package ru.practicum.shareit.request.entity;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.practicum.shareit.user.entity.UserEntity;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ItemRequestEntityTest {

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void whenSaveItemRequest_thenItemRequestIsSaved() {
        UserEntity user = new UserEntity();
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        entityManager.persist(user);

        ItemRequestEntity itemRequest = new ItemRequestEntity();
        itemRequest.setDescription("Need a drill");
        itemRequest.setRequestor(user);
        itemRequest.setCreated(Timestamp.valueOf(LocalDateTime.now()));
        ItemRequestEntity savedItemRequest = entityManager.persistFlushFind(itemRequest);

        assertThat(savedItemRequest).isNotNull();
        assertThat(savedItemRequest.getId()).isNotNull();
        assertThat(savedItemRequest.getDescription()).isEqualTo("Need a drill");
        assertThat(savedItemRequest.getRequestor()).isEqualTo(user);
    }

    @Test
    public void whenFindItemRequestById_thenItemRequestIsFound() {
        UserEntity user = new UserEntity();
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        entityManager.persist(user);

        ItemRequestEntity itemRequest = new ItemRequestEntity();
        itemRequest.setDescription("Need a drill");
        itemRequest.setRequestor(user);
        itemRequest.setCreated(Timestamp.valueOf(LocalDateTime.now()));
        ItemRequestEntity savedItemRequest = entityManager.persistFlushFind(itemRequest);

        ItemRequestEntity foundItemRequest = entityManager.find(ItemRequestEntity.class, savedItemRequest.getId());

        assertThat(foundItemRequest).isEqualTo(savedItemRequest);
    }

    @Test
    public void whenUpdateItemRequest_thenItemRequestIsUpdated() {
        UserEntity user = new UserEntity();
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        entityManager.persist(user);

        ItemRequestEntity itemRequest = new ItemRequestEntity();
        itemRequest.setDescription("Need a drill");
        itemRequest.setRequestor(user);
        itemRequest.setCreated(Timestamp.valueOf(LocalDateTime.now()));
        ItemRequestEntity savedItemRequest = entityManager.persistFlushFind(itemRequest);

        savedItemRequest.setDescription("Need a hammer");
        entityManager.persistAndFlush(savedItemRequest);

        ItemRequestEntity updatedItemRequest = entityManager.find(ItemRequestEntity.class, savedItemRequest.getId());

        assertThat(updatedItemRequest.getDescription()).isEqualTo("Need a hammer");
    }

    @Test
    public void whenDeleteItemRequest_thenItemRequestIsDeleted() {
        UserEntity user = new UserEntity();
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        entityManager.persist(user);

        ItemRequestEntity itemRequest = new ItemRequestEntity();
        itemRequest.setDescription("Need a drill");
        itemRequest.setRequestor(user);
        itemRequest.setCreated(Timestamp.valueOf(LocalDateTime.now()));
        ItemRequestEntity savedItemRequest = entityManager.persistFlushFind(itemRequest);

        entityManager.remove(savedItemRequest);
        entityManager.flush();

        ItemRequestEntity deletedItemRequest = entityManager.find(ItemRequestEntity.class, savedItemRequest.getId());

        assertThat(deletedItemRequest).isNull();
    }

}
