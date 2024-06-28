package ru.practicum.shareit.request.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.request.entity.ItemRequestEntity;
import ru.practicum.shareit.user.entity.UserEntity;
import ru.practicum.shareit.user.repository.UserRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@DataJpaTest
public class ItemRequestRepositoryTest {

    @Autowired
    private ItemRequestRepository itemRequestRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    public void testFindByRequestorId() {
        UserEntity userEntity = new UserEntity();
        userEntity.setName("name");
        userEntity.setEmail("asd@gogo.ru");

        userRepository.save(userEntity);

        ItemRequestEntity requestEntity = new ItemRequestEntity();
        requestEntity.setRequestor(userEntity);
        requestEntity.setCreated(Timestamp.valueOf(LocalDateTime.now()));
        requestEntity.setDescription("test");

        itemRequestRepository.save(requestEntity);

        Long userId = userEntity.getId();
        List<ItemRequestEntity> requestEntities = itemRequestRepository.findByRequestorId(userId);

        Assertions.assertFalse(requestEntities.isEmpty());
        Assertions.assertEquals(userId, requestEntities.get(0).getRequestor().getId());
    }

    @Test
    public void testFindByRequestorIdNot() {
        UserEntity userEntity = new UserEntity();
        userEntity.setName("name");
        userEntity.setEmail("asd@gogo.ru");


        UserEntity userEntity2 = new UserEntity();
        userEntity2.setName("name");
        userEntity2.setEmail("asd@gogoasdasfa.ru");

        userRepository.save(userEntity);
        userRepository.save(userEntity2);

        ItemRequestEntity requestEntity = new ItemRequestEntity();
        requestEntity.setRequestor(userEntity);
        requestEntity.setCreated(Timestamp.valueOf(LocalDateTime.now()));
        requestEntity.setDescription("test");

        itemRequestRepository.save(requestEntity);

        Long userId = 3L;
        Page<ItemRequestEntity> page = itemRequestRepository.findByRequestorIdNot(userId, Pageable.unpaged());

        Assertions.assertFalse(page.isEmpty());
        Assertions.assertNotEquals(userId, page.getContent().get(0).getRequestor().getId());
    }
}
