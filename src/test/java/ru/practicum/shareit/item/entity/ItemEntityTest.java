package ru.practicum.shareit.item.entity;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.practicum.shareit.user.entity.UserEntity;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ItemEntityTest {

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void whenSaveItem_thenItemIsSaved() {
        UserEntity user = new UserEntity();
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        entityManager.persist(user);

        ItemEntity item = new ItemEntity();
        item.setName("Drill");
        item.setDescription("Powerful electric drill");
        item.setAvailable(true);
        item.setOwner(user);

        ItemEntity savedItem = entityManager.persistFlushFind(item);

        assertThat(savedItem).isNotNull();
        assertThat(savedItem.getId()).isNotNull();
        assertThat(savedItem.getName()).isEqualTo("Drill");
        assertThat(savedItem.getDescription()).isEqualTo("Powerful electric drill");
        assertThat(savedItem.getAvailable()).isTrue();
        assertThat(savedItem.getOwner()).isEqualTo(user);
    }

    @Test
    public void whenFindItemById_thenItemIsFound() {
        UserEntity user = new UserEntity();
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        entityManager.persist(user);

        ItemEntity item = new ItemEntity();
        item.setName("Drill");
        item.setDescription("Powerful electric drill");
        item.setAvailable(true);
        item.setOwner(user);
        ItemEntity savedItem = entityManager.persistFlushFind(item);

        ItemEntity foundItem = entityManager.find(ItemEntity.class, savedItem.getId());

        assertThat(foundItem).isEqualTo(savedItem);
    }

    @Test
    public void whenUpdateItem_thenItemIsUpdated() {
        UserEntity user = new UserEntity();
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        entityManager.persist(user);

        ItemEntity item = new ItemEntity();
        item.setName("Drill");
        item.setDescription("Powerful electric drill");
        item.setAvailable(true);
        item.setOwner(user);
        ItemEntity savedItem = entityManager.persistFlushFind(item);

        savedItem.setName("Updated Drill");
        savedItem.setDescription("Updated description");
        entityManager.persistAndFlush(savedItem);

        ItemEntity updatedItem = entityManager.find(ItemEntity.class, savedItem.getId());
        assertThat(updatedItem.getName()).isEqualTo("Updated Drill");
        assertThat(updatedItem.getDescription()).isEqualTo("Updated description");
    }

    @Test
    public void whenDeleteItem_thenItemIsDeleted() {
        UserEntity user = new UserEntity();
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        entityManager.persist(user);

        ItemEntity item = new ItemEntity();
        item.setName("Drill");
        item.setDescription("Powerful electric drill");
        item.setAvailable(true);
        item.setOwner(user);
        ItemEntity savedItem = entityManager.persistFlushFind(item);

        entityManager.remove(savedItem);
        entityManager.flush();

        ItemEntity deletedItem = entityManager.find(ItemEntity.class, savedItem.getId());

        assertThat(deletedItem).isNull();
    }
}