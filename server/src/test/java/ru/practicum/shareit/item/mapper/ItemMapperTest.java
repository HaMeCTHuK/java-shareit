package ru.practicum.shareit.item.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

public class ItemMapperTest {

    @InjectMocks
    private ItemMapper mapper = Mappers.getMapper(ItemMapper.class);
    @Mock
    private UserMapper userMapper;
    private ItemDto itemDto;
    private Item item;
    private User user;
    private UserDto userDto;
    private ItemRequest itemRequest;
    private Booking booking;
    private Item.ItemBooking itemBooking;
    private Item.ItemComment comment;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);
        user.setEmail("123@ttt.com");
        user.setName("name");

        userDto = new UserDto();
        userDto.setId(1L);
        userDto.setEmail("1adasd23@ttsdat.com");
        userDto.setName("names");

        User user2 = new User();
        user2.setId(2L);
        user2.setEmail("1231231234@ttt.com");
        user2.setName("namessss");

        comment = new Item.ItemComment(1L,"tratata",1L,1L,"name",LocalDateTime.now());

        List<Item.ItemComment> commentSet = new ArrayList<>();
        commentSet.add(comment);

        itemRequest = new ItemRequest();
        itemRequest.setRequestor(user);
        itemRequest.setCreated(LocalDateTime.now());
        itemRequest.setId(1L);
        itemRequest.setDescription("test");

        itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("Test Item");
        itemDto.setDescription("Test Description");
        itemDto.setRequestId(1L);
        itemDto.setOwner(userDto);
        itemDto.setComments(commentSet);
        itemDto.setAvailable(true);
        itemDto.setRequestId(1L);
        itemDto.setLastBooking(itemBooking);
        itemDto.setNextBooking(itemBooking);

        booking = new Booking();
        booking.setId(1L);
        booking.setStart(LocalDateTime.now());
        booking.setEnd(LocalDateTime.now().plusHours(1));
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(BookingStatus.APPROVED);

        itemBooking = new Item.ItemBooking(
                1L,
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(1),
                1L,
                1L,
                BookingStatus.APPROVED);

        item = new Item();
        item.setId(1L);
        item.setName("Test Item");
        item.setDescription("Test Description");
        item.setRequest(itemRequest);
        item.setOwner(user);
        item.setAvailable(true);
        item.setNextBooking(itemBooking);
        item.setLastBooking(itemBooking);
        item.setComments(commentSet);
    }

    @Test
    public void testToItemFromItemDtoCreate() {
        Item mappedItem = mapper.toItemFromItemDtoCreate(itemDto, 1L);

        assertNotNull(mappedItem);
        assertEquals(itemDto.getId(), mappedItem.getId());
        assertEquals(itemDto.getName(), mappedItem.getName());
        assertEquals(itemDto.getDescription(), mappedItem.getDescription());
        assertEquals(itemDto.getRequestId(), mappedItem.getRequest().getId());
        assertEquals(1L, mappedItem.getOwner().getId());
    }

    @Test
    public void testToItemDtoFromUpdateRequest() {
        Item mappedItem = mapper.toItemDtoFromUpdateRequest(itemDto, 1L);

        assertNotNull(mappedItem);
        assertEquals(itemDto.getName(), mappedItem.getName());
        assertEquals(itemDto.getDescription(), mappedItem.getDescription());
        assertEquals(1L, mappedItem.getOwner().getId());
    }

    @Test
    public void testToItemsList() {
        List<ItemDto> itemDtoList = new ArrayList<>();
        itemDtoList.add(itemDto);

        when(userMapper.toUser(userDto)).thenReturn(user);
        List<Item> itemList = mapper.toItemsList(itemDtoList);

        assertNotNull(itemList);
        assertEquals(1, itemList.size());

        Item mappedItem = itemList.get(0);
        assertEquals(itemDto.getId(), mappedItem.getId());
        assertEquals(itemDto.getName(), mappedItem.getName());
        assertEquals(itemDto.getDescription(), mappedItem.getDescription());
    }

    @Test
    public void testToItemsDtoList() {
        List<Item> itemList = new ArrayList<>();
        itemList.add(item);

        when(userMapper.toUser(userDto)).thenReturn(user);
        List<ItemDto> itemDtoList = mapper.toItemsDtoList(itemList);

        assertNotNull(itemDtoList);
        assertEquals(1, itemDtoList.size());

        ItemDto mappedDto = itemDtoList.get(0);
        assertEquals(item.getId(), mappedDto.getId());
        assertEquals(item.getName(), mappedDto.getName());
        assertEquals(item.getDescription(), mappedDto.getDescription());
        assertEquals(item.getRequest().getId(), mappedDto.getRequestId());
    }

    @Test
    public void testItemDtoToItem() {
        when(userMapper.toUser(userDto)).thenReturn(user);
        Item mappedItem = mapper.itemDtoToItem(itemDto);

        assertNotNull(mappedItem);
        assertEquals(itemDto.getId(), mappedItem.getId());
        assertEquals(itemDto.getName(), mappedItem.getName());
        assertEquals(itemDto.getDescription(), mappedItem.getDescription());
    }

    @Test
    public void testToItemDto() {
        when(userMapper.toUser(userDto)).thenReturn(user);
        ItemDto mappedDto = mapper.toItemDto(item);

        assertNotNull(mappedDto);
        assertEquals(item.getId(), mappedDto.getId());
        assertEquals(item.getName(), mappedDto.getName());
        assertEquals(item.getDescription(), mappedDto.getDescription());
        assertEquals(item.getRequest().getId(), mappedDto.getRequestId());
    }

}
