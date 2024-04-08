package ru.practicum.shareit.item.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.entity.ItemEntity;
import ru.practicum.shareit.request.entity.ItemRequestEntity;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.entity.UserEntity;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRequestRepository itemRequestRepository;

    @Test
    public void testFindAllByRequestId() {

        UserEntity userEntity = new UserEntity();
        userEntity.setName("name");
        userEntity.setEmail("asd@gogo.ru");

        UserEntity savedUserEntity = userRepository.save(userEntity);

        ItemRequestEntity requestEntity = new ItemRequestEntity();
        requestEntity.setRequestor(savedUserEntity);
        requestEntity.setCreated(Timestamp.valueOf(LocalDateTime.now()));
        requestEntity.setDescription("test");

        ItemRequestEntity savedItemRequestEntity = itemRequestRepository.save(requestEntity);

        ItemEntity itemEntity = new ItemEntity();
        itemEntity.setAvailable(true);
        itemEntity.setRequest(savedItemRequestEntity);
        itemEntity.setName("name");
        itemEntity.setDescription("disc");
        itemEntity.setOwner(savedUserEntity);

        itemRepository.save(itemEntity);

        Long itemRequestsId = 1L;

        List<ItemEntity> items = itemRepository.findAllByRequestId(itemRequestsId);

        assertEquals(1, items.size());

    }

    @Test
    public void testSearch() {

        String searchText = "example";

        UserEntity userEntity = new UserEntity();
        userEntity.setName("name");
        userEntity.setEmail("asd@gogo.ru");

        UserEntity savedUserEntity = userRepository.save(userEntity);

        ItemRequestEntity requestEntity = new ItemRequestEntity();
        requestEntity.setRequestor(savedUserEntity);
        requestEntity.setCreated(Timestamp.valueOf(LocalDateTime.now()));
        requestEntity.setDescription("test");

        ItemRequestEntity savedItemRequestEntity = itemRequestRepository.save(requestEntity);

        ItemEntity itemEntity = new ItemEntity();
        itemEntity.setAvailable(true);
        itemEntity.setRequest(savedItemRequestEntity);
        itemEntity.setName("name");
        itemEntity.setDescription("example");
        itemEntity.setOwner(userEntity);

        itemRepository.save(itemEntity);

        List<ItemEntity> items = itemRepository.search(searchText);

        assertEquals(1, items.size());
    }

}
