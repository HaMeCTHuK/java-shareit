package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.DataNotFoundException;
import ru.practicum.shareit.exception.StorageException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.entity.ItemEntity;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.mapper.ItemRepositoryMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.entity.ItemRequestEntity;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.entity.UserEntity;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.mapper.UserRepositoryMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class ItemServiceImplTest {

    @Mock
    private UserMapper userMapper;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private ItemRepositoryMapper itemRepositoryMapper;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserRepositoryMapper userRepositoryMapper;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private BookingMapper bookingMapper;

    @Mock
    private CommentMapper commentMapper;

    @Mock
    private UserService userService;

    @Mock
    private ItemMapper itemMapper;

    @Mock
    private ItemRequestRepository itemRequestRepository;

    @Mock
    private ItemRequestMapper itemRequestMapper;

    @InjectMocks
    private ItemServiceImpl itemService;

    @Test
    void createItem_ValidInput_ReturnsItemDto() {
        Long ownerId = 1L;
        UserEntity userEntity = new UserEntity();
        userEntity.setId(ownerId);
        userEntity.setEmail("123@123.ru");
        userEntity.setName("boss");

        User user = new User();
        user.setId(ownerId);
        user.setEmail("123@123.ru");
        user.setName("test");

        UserDto userDto = new UserDto();
        userDto.setId(ownerId);
        userDto.setEmail("123@123.ru");
        userDto.setName("test");

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(2L);
        itemRequest.setCreated(LocalDateTime.now());
        itemRequest.setRequestor(user);
        itemRequest.setDescription("asdasfas");

        ItemRequestEntity itemRequestEntity = new ItemRequestEntity();
        itemRequest.setId(2L);
        itemRequest.setCreated(LocalDateTime.now());
        itemRequest.setRequestor(user);
        itemRequest.setDescription("asdasfas");

        Item item = new Item();
        item.setOwner(user);
        item.setName("1234");
        item.setAvailable(true);
        item.setDescription("123124124");
        item.setRequest(itemRequest);

        ItemDto itemDto = new ItemDto();
        itemDto.setId(2L);
        itemDto.setOwner(userDto);
        itemDto.setName("1234");
        itemDto.setAvailable(true);
        itemDto.setDescription("123124124");

        ItemEntity itemEntity = new ItemEntity();
        itemEntity.setId(1L);
        itemEntity.setOwner(userEntity);
        itemEntity.setName("123123");
        itemEntity.setDescription("1231241241");
        itemEntity.setAvailable(true);
        itemEntity.setRequest(itemRequestEntity);

        when(userService.getUser(ownerId)).thenReturn(userDto);
        when(itemRepository.save(any(ItemEntity.class))).thenReturn(itemEntity);
        when(itemRepositoryMapper.toItem(any(ItemEntity.class))).thenReturn(new Item());
        when(itemMapper.toItemDto(any())).thenReturn(itemDto);

        //ItemDto createdItem = itemService.createItem(item);

        //assertNotNull(createdItem);
        //assertEquals(ownerId, createdItem.getOwner().getId());
    }

    @Test
    void updateItem_ValidInput_ReturnsUpdatedItemDto() {
        Long userid = 6L;
        UserDto userDto = new UserDto();
        userDto.setId(userid);
        Long itemId = 1L;
        ItemDto itemDto = new ItemDto();
        itemDto.setId(itemId);
        itemDto.setOwner(userDto);
        ItemEntity storedItem = new ItemEntity();
        storedItem.setId(itemId);
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(storedItem));
        when(itemRepository.save(any(ItemEntity.class))).thenReturn(storedItem);
        when(itemRepositoryMapper.toItem(any())).thenReturn(new Item());
        when(itemMapper.toItemDto(any(Item.class))).thenReturn(itemDto);

        ItemDto updatedItem = itemService.updateItem(itemId, itemDto);

        assertNotNull(updatedItem);
        assertEquals(itemId, updatedItem.getId());
    }

    @Test
    void updateItem_NonExistingItem_ThrowsDataNotFoundException() {
        Long itemId = 999L;
        ItemDto itemDto = new ItemDto();
        itemDto.setId(itemId);
        when(itemRepository.findById(itemId)).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> itemService.updateItem(itemId, itemDto));
    }

    @Test
    void getItem_ExistingItemId_ReturnsItemDto() {
        Long itemId = 1L;
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setEmail("123@123.ru");
        user.setName("test");

        ItemRequestEntity itemRequest = new ItemRequestEntity();
        itemRequest.setId(2L);
        itemRequest.setCreated(Timestamp.valueOf(LocalDateTime.now()));
        itemRequest.setRequestor(user);
        itemRequest.setDescription("asdasfas");

        ItemEntity item = new ItemEntity();
        item.setId(itemId);
        item.setOwner(user);
        item.setName("1234");
        item.setAvailable(true);
        item.setDescription("testing");
        item.setRequest(itemRequest);

        ItemDto iDto = new ItemDto();
        iDto.setId(itemId);
        iDto.setName("1234");
        iDto.setAvailable(true);
        iDto.setDescription("testing");


        when(itemRepository.existsById(itemId)).thenReturn(true);
        when(itemRepository.getReferenceById(itemId)).thenReturn(item);
        when(itemRepositoryMapper.toItem(any())).thenReturn(new Item());
        when(itemMapper.toItemDto(any(Item.class))).thenReturn(iDto);

        ItemDto itemDto = itemService.getItem(itemId);

        assertNotNull(itemDto);
        assertEquals(itemId, itemDto.getId());
    }

    @Test
    void getItem_NonExistingItemId_ThrowsDataNotFoundException() {
        Long itemId = 999L;
        when(itemRepository.existsById(itemId)).thenReturn(false);

        assertThrows(DataNotFoundException.class, () -> itemService.getItem(itemId));
    }

    @Test
    void getAllItemsWithUserId_UserNotFound() {
        Long userId = 1L;
        when(userRepository.existsById(userId)).thenReturn(false);

        assertThrows(DataNotFoundException.class, () -> itemService.getAllItemsWithUserId(userId));
    }

    @Test
    void getItemWithUserId_ItemNotFound() {
        Long itemId = 1L;
        Long userId = 1L;
        when(itemRepository.existsById(itemId)).thenReturn(false);

        assertThrows(DataNotFoundException.class, () -> itemService.getItemWithUserId(itemId, userId));
    }

    @Test
    void searchItemsByText() {
        String searchText = "test";

        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setEmail("123@123.ru");
        user.setName("test");

        ItemRequestEntity itemRequest = new ItemRequestEntity();
        itemRequest.setId(2L);
        itemRequest.setCreated(Timestamp.valueOf(LocalDateTime.now()));
        itemRequest.setRequestor(user);
        itemRequest.setDescription("asdasfas");

        ItemEntity item = new ItemEntity();
        item.setId(2L);
        item.setOwner(user);
        item.setName("1234");
        item.setAvailable(true);
        item.setDescription("testing");
        item.setRequest(itemRequest);



        when(itemRepository.findAll()).thenReturn(Collections.singletonList(item));
        when(itemRepositoryMapper.toItem(any())).thenReturn(new Item());
        when(itemMapper.toItemDto(any())).thenReturn(new ItemDto());

        List<ItemDto> foundItems = itemService.searchItemsByText(searchText, 1L);

        assertNotNull(foundItems);
        assertFalse(foundItems.isEmpty());
    }

    @Test
    void addComment_ValidationException() {
        Comment comment = new Comment();
        comment.setItemId(1L);
        comment.setAuthorId(1L);

        ItemEntity itemEntity = new ItemEntity();
        UserEntity userEntity = new UserEntity();

        when(itemRepository.findById(comment.getItemId())).thenReturn(Optional.of(itemEntity));
        when(userRepository.findById(comment.getAuthorId())).thenReturn(Optional.of(userEntity));
        when(commentRepository.save(any())).thenThrow(StorageException.class);

        assertThrows(ValidationException.class, () -> itemService.addComment(comment));
    }

}
