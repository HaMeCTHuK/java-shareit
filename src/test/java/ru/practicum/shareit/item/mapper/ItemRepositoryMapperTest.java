package ru.practicum.shareit.item.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.entity.BookingEntity;
import ru.practicum.shareit.item.entity.ItemEntity;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemResponseOnRequestDto;
import ru.practicum.shareit.request.entity.ItemRequestEntity;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.entity.UserEntity;
import ru.practicum.shareit.user.model.User;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ItemRepositoryMapperTest {

    private final ItemRepositoryMapper mapper = Mappers.getMapper(ItemRepositoryMapper.class);
    private ItemEntity itemEntity;
    private Item item;
    private Item updatedItem;
    private BookingEntity bookingEntity;
    private ItemRequestEntity itemRequestEntity;
    private UserEntity userEntity;
    private User user;
    private ItemRequest itemRequest;
    private Item.ItemBooking itemBooking;

    @BeforeEach
    void setUp() {

        user = new User();
        user.setId(1L);
        user.setEmail("123@ttt.com");
        user.setName("name");

        User user2 = new User();
        user2.setId(2L);
        user2.setEmail("1231231234@ttt.com");
        user2.setName("namessss");

        itemRequest = new ItemRequest();
        itemRequest.setRequestor(user);
        itemRequest.setCreated(LocalDateTime.now());
        itemRequest.setId(1L);

        itemRequestEntity = new ItemRequestEntity();
        itemRequestEntity.setRequestor(userEntity);
        itemRequestEntity.setCreated(Timestamp.valueOf(LocalDateTime.now()));
        itemRequestEntity.setId(1L);

        itemBooking = new Item.ItemBooking(
                1L,
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(1),
                1L,
                1L,
                BookingStatus.APPROVED);

        updatedItem = new Item();
        updatedItem.setName("tem");
        updatedItem.setDescription("Description");
        updatedItem.setAvailable(false);
        updatedItem.setOwner(user);
        updatedItem.setRequest(itemRequest);
        updatedItem.setNextBooking(itemBooking);
        updatedItem.setLastBooking(itemBooking);

        itemEntity = new ItemEntity();
        itemEntity.setId(1L);
        itemEntity.setName("Test");
        itemEntity.setDescription("Test");
        itemEntity.setAvailable(true);
        itemEntity.setRequest(itemRequestEntity);

        userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setEmail("test@mail.com");
        userEntity.setName("Lesha");

        item = new Item();
        item.setId(1L);
        item.setName("Test Item");
        item.setDescription("Test Description");
        item.setAvailable(true);
        item.setOwner(user2);
        item.setLastBooking(itemBooking);
        item.setNextBooking(itemBooking);

        bookingEntity = new BookingEntity();
        bookingEntity.setId(1L);
        bookingEntity.setStart(Timestamp.valueOf(LocalDateTime.now()));
        bookingEntity.setEnd(Timestamp.valueOf(LocalDateTime.now().plusHours(1)));
        bookingEntity.setItem(itemEntity);
        bookingEntity.setBooker(userEntity);
        bookingEntity.setStatus(BookingStatus.APPROVED);

    }

    @Test
    public void testToItem() {
        Item mappedItem = mapper.toItem(itemEntity);

        assertNotNull(mappedItem);
        assertEquals(itemEntity.getId(), mappedItem.getId());
        assertEquals(itemEntity.getName(), mappedItem.getName());
        assertEquals(itemEntity.getDescription(), mappedItem.getDescription());
        assertEquals(itemEntity.getAvailable(), mappedItem.getAvailable());
    }

    @Test
    public void testToEntity() {
        ItemEntity mappedEntity = mapper.toEntity(item);

        assertNotNull(mappedEntity);
        assertEquals(item.getId(), mappedEntity.getId());
        assertEquals(item.getName(), mappedEntity.getName());
        assertEquals(item.getDescription(), mappedEntity.getDescription());
        assertEquals(item.getAvailable(), mappedEntity.getAvailable());
    }

    @Test
    public void testUpdateEntity() {
        mapper.updateEntity(updatedItem, itemEntity);

        assertEquals(updatedItem.getName(), itemEntity.getName());
        assertEquals(updatedItem.getDescription(), itemEntity.getDescription());
        assertEquals(updatedItem.getAvailable(), itemEntity.getAvailable());
    }

    @Test
    public void testToItemBooking() {
        Item.ItemBooking itemBooking = mapper.toItemBooking(bookingEntity);

        assertNotNull(itemBooking);
        assertEquals(bookingEntity.getItem().getId(), itemBooking.getItemId());
        assertEquals(bookingEntity.getBooker().getId(), itemBooking.getBookerId());
        assertEquals(bookingEntity.getStart().toLocalDateTime(), itemBooking.getStart());
        assertEquals(bookingEntity.getEnd().toLocalDateTime(), itemBooking.getEnd());
    }

    @Test
    public void testItemEntityToItemResponseOnRequestDto() {
        ItemResponseOnRequestDto responseDto = mapper.itemEntityToItemResponseOnRequestDto(itemEntity);

        assertNotNull(responseDto);
        assertEquals(itemEntity.getId(), responseDto.getId());
        assertEquals(itemEntity.getName(), responseDto.getName());
        assertEquals(itemEntity.getDescription(), responseDto.getDescription());
    }

    @Test
    public void testToItemsResponseListFromEntity() {
        List<ItemEntity> itemEntityList = new ArrayList<>();
        itemEntityList.add(itemEntity);

        List<ItemResponseOnRequestDto> responseDtoList = mapper.toItemsResponseListFromEntity(itemEntityList);

        assertNotNull(responseDtoList);
        assertEquals(1, responseDtoList.size());

        ItemResponseOnRequestDto responseDto = responseDtoList.get(0);
        assertEquals(itemEntity.getId(), responseDto.getId());
        assertEquals(itemEntity.getName(), responseDto.getName());
        assertEquals(itemEntity.getDescription(), responseDto.getDescription());
    }

    @Test
    public void testToTimestamp() {
        LocalDateTime localDateTime = LocalDateTime.now();
        Timestamp mappedTimestamp = mapper.toTimeStamp(localDateTime);
        assertEquals(mappedTimestamp.toLocalDateTime(),localDateTime);
    }
}
