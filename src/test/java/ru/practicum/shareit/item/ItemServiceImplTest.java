package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.entity.BookingEntity;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.DataNotFoundException;
import ru.practicum.shareit.exception.StorageException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.entity.CommentEntity;
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
import java.util.ArrayList;
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
    void createItem_ValidItemDto() {
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
        itemRequestEntity.setId(2L);
        itemRequestEntity.setCreated(Timestamp.valueOf(LocalDateTime.now()));
        itemRequestEntity.setRequestor(userEntity);
        itemRequestEntity.setDescription("asdasfas");

        Item item = new Item();
        item.setId(1L);
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
        when(itemRepositoryMapper.toEntity(item)).thenReturn(itemEntity);
        when(itemRequestRepository.getReferenceById(any(Long.class))).thenReturn(itemRequestEntity);
        when(itemRepository.save(any(ItemEntity.class))).thenReturn(itemEntity);
        when(itemRepositoryMapper.toItem(itemEntity)).thenReturn(item);
        when(itemMapper.toItemDto(item)).thenReturn(itemDto);

        ItemDto createdItem = itemService.createItem(item);

        assertNotNull(createdItem);
        assertEquals(ownerId, createdItem.getOwner().getId());
    }

    @Test
    void updateItem_ValidItemDto() {
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
    void updateItem_DataNotFoundException() {
        Long itemId = 999L;
        ItemDto itemDto = new ItemDto();
        itemDto.setId(itemId);
        when(itemRepository.findById(itemId)).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> itemService.updateItem(itemId, itemDto));
    }

    @Test
    void getItem_ReturnsItemDto() {
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
    void getItem_ThrowsDataNotFoundException() {
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

    @Test
    void deleteItem_ValidItemId() {
        Long itemId = 1L;
        ItemEntity itemEntity = new ItemEntity();
        itemEntity.setId(itemId);

        when(itemRepository.existsById(itemId)).thenReturn(true);
        when(itemRepository.getReferenceById(itemId)).thenReturn(itemEntity);

        assertDoesNotThrow(() -> itemService.deleteItem(itemId));
    }

    @Test
    void deleteItem_ItemNotFound() {
        Long itemId = 999L;
        when(itemRepository.existsById(itemId)).thenReturn(false);

        assertThrows(DataNotFoundException.class, () -> itemService.deleteItem(itemId));
    }

    @Test
    void getAllItems_ReturnsAllItems() {
        List<ItemEntity> itemEntities = new ArrayList<>();
        itemEntities.add(new ItemEntity());
        itemEntities.add(new ItemEntity());

        when(itemRepository.findAll()).thenReturn(itemEntities);
        when(itemRepositoryMapper.toItem(any())).thenReturn(new Item());
        when(itemMapper.toItemDto(any())).thenReturn(new ItemDto());

        List<ItemDto> allItems = itemService.getAllItems();

        assertNotNull(allItems);
        assertEquals(itemEntities.size(), allItems.size());
    }

    @Test
    void getAllItems_NoItemsFound() {
        when(itemRepository.findAll()).thenReturn(Collections.emptyList());

        List<ItemDto> allItems = itemService.getAllItems();

        assertNotNull(allItems);
        assertTrue(allItems.isEmpty());
    }

    @Test
    void getAllItemsWithUserId_ValidUserId() {
        Long userId = 1L;
        UserDto userDto = new UserDto();
        userDto.setId(userId);
        userDto.setName("Aby");
        userDto.setEmail("asd@asd.com");

        when(userRepository.existsById(userId)).thenReturn(true);
        when(userService.getUser(userId)).thenReturn(userDto);
        when(userRepositoryMapper.toEntity(any(User.class))).thenReturn(new UserEntity());
        when(itemRepository.findAllByOwnerOrderById(any())).thenReturn(Collections.emptyList());

        assertDoesNotThrow(() -> itemService.getAllItemsWithUserId(userId));
    }

    @Test
    void getItemWithUserId_ValidItemIdAndUserId() {
        Long itemId = 1L;
        Long userId = 1L;

        UserEntity userEntity = new UserEntity();
        userEntity.setId(userId);
        userEntity.setEmail("123@123.ru");
        userEntity.setName("test");

        User user = new User();
        user.setId(userId);
        user.setEmail("123@123.ru");
        user.setName("test");

        ItemRequestEntity itemRequest = new ItemRequestEntity();
        itemRequest.setId(2L);
        itemRequest.setCreated(Timestamp.valueOf(LocalDateTime.now()));
        itemRequest.setRequestor(userEntity);
        itemRequest.setDescription("asdasfas");

        ItemEntity itemEntity = new ItemEntity();
        itemEntity.setId(itemId);
        itemEntity.setOwner(userEntity);
        itemEntity.setName("1234");
        itemEntity.setAvailable(true);
        itemEntity.setDescription("testing");
        itemEntity.setRequest(itemRequest);

        ItemDto iDto = new ItemDto();
        iDto.setId(itemId);
        iDto.setName("1234");
        iDto.setAvailable(true);
        iDto.setDescription("testing");

        Item item = new Item();
        item.setId(itemId);
        item.setName("1234");
        item.setAvailable(true);
        item.setDescription("testing");
        item.setOwner(user);

        when(itemRepository.existsById(itemId)).thenReturn(true);
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(itemEntity));
        when(userRepository.existsById(userId)).thenReturn(true);
        when(bookingRepository.findAllByItemId(itemId)).thenReturn(Collections.emptyList());
        when(itemRepositoryMapper.toItem(itemEntity)).thenReturn(item);
        when(commentRepository.findAllByItemId(itemId)).thenReturn(Collections.emptyList());
        when(commentMapper.toItemComment(new CommentEntity())).thenReturn(
                new Item.ItemComment(1L,"ok", 1L, 1L, "ok", LocalDateTime.now()));

        assertDoesNotThrow(() -> itemService.getItemWithUserId(itemId, userId));

        Item recivedItem = itemService.getItemWithUserId(itemId, userId);
        assertNotNull(recivedItem);
    }

    @Test
    void addComment_ValidComment() {
        Comment comment = new Comment();
        comment.setItemId(1L);
        comment.setAuthorId(1L);
        ItemEntity itemEntity = new ItemEntity();
        UserEntity userEntity = new UserEntity();
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setStart(LocalDateTime.now());
        booking.isFinished(LocalDateTime.now());
        booking.setStatus(BookingStatus.APPROVED);
        booking.setEnd(LocalDateTime.now());

        BookingEntity bookingEntity = new BookingEntity();
        bookingEntity.setId(1L);
        bookingEntity.setStart(Timestamp.valueOf(LocalDateTime.now()));

        List<BookingEntity> recivedList = new ArrayList<>();
        recivedList.add(bookingEntity);

        when(itemRepository.findById(comment.getItemId())).thenReturn(Optional.of(itemEntity));
        when(userRepository.findById(comment.getAuthorId())).thenReturn(Optional.of(userEntity));
        when(bookingRepository.findAllByItemAndBooker(any(), any())).thenReturn(Collections.singletonList(bookingEntity));
        when(bookingMapper.toBooking(any(BookingEntity.class))).thenReturn(booking);
        when(commentMapper.toCommentEntity(any())).thenReturn(new CommentEntity());
        when(commentRepository.save(any())).thenReturn(new CommentEntity());
        when(commentMapper.toCommentDto(any(CommentEntity.class))).thenReturn(new CommentDto());

        assertDoesNotThrow(() -> itemService.addComment(comment));
    }

}
