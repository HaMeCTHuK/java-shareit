package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.entity.BookingEntity;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.DataNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.entity.ItemEntity;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.entity.ItemRequestEntity;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.entity.UserEntity;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class BookingServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private BookingMapper bookingMapper;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BookingServiceImpl bookingService;

    private BookingEntity bookingEntity;
    private UserEntity userEntityOwner;
    private UserEntity userEntityRequestor;
    private ItemEntity itemEntity;
    private ItemRequestEntity itemRequestEntity;
    private BookingDto bookingDto;
    private Booking booking;
    private User user;
    private  Item item;

    @BeforeEach
    void setUp() {
        userEntityOwner = new UserEntity();
        userEntityOwner.setId(1L);
        userEntityOwner.setEmail("123@123.ru");
        userEntityOwner.setName("boss");

        userEntityRequestor = new UserEntity();
        userEntityRequestor.setId(2L);
        userEntityRequestor.setEmail("123@123.ru");
        userEntityRequestor.setName("boss");

        itemRequestEntity = new ItemRequestEntity();
        itemRequestEntity.setId(2L);
        itemRequestEntity.setCreated(Timestamp.valueOf(LocalDateTime.now()));
        itemRequestEntity.setRequestor(userEntityRequestor);
        itemRequestEntity.setDescription("asdasfas");

        itemEntity = new ItemEntity();
        itemEntity.setId(1L);
        itemEntity.setOwner(userEntityOwner);
        itemEntity.setName("123123");
        itemEntity.setDescription("1231241241");
        itemEntity.setAvailable(true);
        itemEntity.setRequest(itemRequestEntity);

        bookingEntity = new BookingEntity();
        bookingEntity.setId(1L);
        bookingEntity.setItem(itemEntity);
        bookingEntity.setBooker(userEntityRequestor);
        bookingEntity.setStatus(BookingStatus.WAITING);

        bookingDto = new BookingDto();
        bookingDto.setStart(LocalDateTime.now().plusDays(1));
        bookingDto.setEnd(LocalDateTime.now().plusDays(2));
        bookingDto.setItem(new ItemDto());
        bookingDto.setBooker(new UserDto());
        bookingDto.setStatus(BookingStatus.APPROVED);

        user = new User();
        user.setId(4L);
        user.setEmail("tet@tet.com");
        user.setName("name");

        item = new Item();
        item.setId(1L);
        item.setOwner(user);
        item.setDescription("testsda");
        item.setName("itemName");
        item.setOwner(user);

        booking = new Booking();
        booking.setStart(LocalDateTime.now().plusDays(1));
        booking.setEnd(LocalDateTime.now().plusDays(2));
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(BookingStatus.APPROVED);



    }

    @Test
    void createBooking_Successful() {
        when(itemRepository.findById(1L)).thenReturn(Optional.of(itemEntity));
        when(userRepository.findById(4L)).thenReturn(Optional.of(userEntityRequestor));
        when(userRepository.findById(3L)).thenReturn(Optional.of(userEntityOwner));
        when(bookingMapper.toEntity(any(Booking.class))).thenReturn(bookingEntity);
        when(bookingRepository.save(any(BookingEntity.class))).thenReturn(bookingEntity);
        when(bookingMapper.toDto(any(BookingEntity.class))).thenReturn(bookingDto);

        BookingDto createdBookingDto = bookingService.createBooking(booking);

        assertNotNull(createdBookingDto);
        verify(bookingRepository, times(1)).save(any(BookingEntity.class));
    }

    @Test
    void getBooking_Successful() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(bookingEntity));
        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntityOwner));
        when(bookingMapper.toDto(any(BookingEntity.class))).thenReturn(new BookingDto());

        BookingDto retrievedBookingDto = bookingService.getBooking(1L, 1L);

        assertNotNull(retrievedBookingDto);
    }

    @Test
    void getBooking_DataNotFoundException() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> bookingService.getBooking(1L, 1L));
    }

    @Test
    void getBooking_DataNotFoundExceptionTest() {
        Long bookingId = 1L;

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> bookingService.getBooking(bookingId, userEntityOwner.getId()));
    }

    @Test
    void updateBooking_Successful() {
        Long bookingId = 1L;

        when(bookingRepository.findByIdWithItemAndBooker(bookingId)).thenReturn(Optional.of(bookingEntity));
        when(bookingRepository.save(bookingEntity)).thenReturn(bookingEntity);
        when(bookingMapper.toDto(bookingEntity)).thenReturn(bookingDto);

        BookingDto updatedBookingDto = bookingService.updateBooking(bookingId, userEntityOwner.getId(), true);

        assertNotNull(updatedBookingDto);
        assertEquals(bookingDto, updatedBookingDto);
    }

    @Test
    void deleteBooking_Successful() {
        Long bookingId = 1L;

        bookingService.deleteBooking(bookingId);

        verify(bookingRepository, times(1)).deleteById(bookingId);
    }

    @Test
    void findAllByOwnerItemsAndStatus_All_Successful() {
        List<BookingEntity> bookingEntities = Collections.singletonList(bookingEntity);

        when(userRepository.findById(userEntityOwner.getId())).thenReturn(Optional.of(userEntityOwner));
        when(bookingRepository.findAllByOwnerItemsAndStatus(userEntityOwner, BookingStatus.ALL, Pageable.unpaged())).thenReturn(bookingEntities);
        when(bookingMapper.toBookingDtoList(bookingEntities)).thenReturn(Collections.singletonList(bookingDto));

        List<BookingDto> bookingDtos = bookingService.findAllByOwnerItemsAndStatus(userEntityOwner.getId(), BookingStatus.ALL,Pageable.unpaged());

        assertNotNull(bookingDtos);
    }

    @Test
    void findAllByOwnerItemsAndStatus_Future_Successful() {
        List<BookingEntity> bookingEntities = Collections.singletonList(bookingEntity);

        when(userRepository.findById(userEntityOwner.getId())).thenReturn(Optional.of(userEntityOwner));
        when(bookingRepository.findFutureByOwnerItems(userEntityOwner, Timestamp.valueOf(LocalDateTime.now()), Pageable.unpaged())).thenReturn(bookingEntities);
        when(bookingMapper.toBookingDtoList(bookingEntities)).thenReturn(Collections.singletonList(bookingDto));

        List<BookingDto> bookingDtos = bookingService.findAllByOwnerItemsAndStatus(userEntityOwner.getId(), BookingStatus.FUTURE, Pageable.unpaged());

        assertNotNull(bookingDtos);
    }

    @Test
    void getUserBookings_Future_Successful() {
        List<BookingEntity> bookingEntities = Collections.singletonList(bookingEntity);

        when(userRepository.findById(userEntityRequestor.getId())).thenReturn(Optional.of(userEntityRequestor));
        when(bookingRepository.findAllByOwnerItemsAndStatus(userEntityRequestor, BookingStatus.FUTURE, Pageable.unpaged())).thenReturn(bookingEntities);
        when(bookingMapper.toBookingDtoList(bookingEntities)).thenReturn(Collections.singletonList(bookingDto));

        List<BookingDto> bookingDtos = bookingService.getUserBookings(userEntityRequestor.getId(), BookingStatus.FUTURE, Pageable.unpaged());

        assertNotNull(bookingDtos);
    }

    @Test
    void getOwnerBookingsByState_Successful() {
        List<BookingEntity> bookingEntities = Collections.singletonList(bookingEntity);

        when(userRepository.findById(userEntityOwner.getId())).thenReturn(Optional.of(userEntityOwner));
        when(bookingRepository.findAllByOwnerItemsAndStatus(userEntityOwner, BookingStatus.WAITING, Pageable.unpaged())).thenReturn(bookingEntities);
        when(bookingMapper.toBookingDtoList(bookingEntities)).thenReturn(Collections.singletonList(bookingDto));

        List<BookingDto> bookingDtos = bookingService.getOwnerBookingsByState(userEntityOwner.getId(), "WAITING",Pageable.unpaged());

        assertNotNull(bookingDtos);
        assertFalse(bookingDtos.isEmpty());
    }

    @Test
    void findAllByOwner_Successful() {
        List<BookingEntity> bookingEntities = Collections.singletonList(bookingEntity);

        when(userRepository.findById(userEntityOwner.getId())).thenReturn(Optional.of(userEntityOwner));
        when(bookingRepository.findAllByOwnerItemsAndStatus(userEntityOwner,BookingStatus.ALL, Pageable.unpaged())).thenReturn(bookingEntities);
        when(bookingMapper.toBookingDtoList(bookingEntities)).thenReturn(Collections.singletonList(bookingDto));

        List<BookingDto> bookingDtos = bookingService.findAllByOwner(userEntityOwner.getId(),Pageable.unpaged());

        assertNotNull(bookingDtos);
        assertFalse(bookingDtos.isEmpty());
    }

    @Test
    void findCurrentByOwnerItems_Successful() {
        List<BookingEntity> bookingEntities = Collections.singletonList(bookingEntity);

        when(userRepository.findById(userEntityOwner.getId())).thenReturn(Optional.of(userEntityOwner));
        when(bookingRepository.findCurrentByOwnerItems(userEntityOwner, Timestamp.valueOf(LocalDateTime.now()), Pageable.unpaged())).thenReturn(bookingEntities);
        when(bookingMapper.toBookingDtoList(bookingEntities)).thenReturn(Collections.singletonList(bookingDto));

        List<BookingDto> bookingDtos = bookingService.findCurrentByOwnerItems(userEntityOwner.getId(),Pageable.unpaged());

        assertNotNull(bookingDtos);
    }

    @Test
    void findPastByOwnerItems_Successful() {
        List<BookingEntity> bookingEntities = Collections.singletonList(bookingEntity);

        when(userRepository.findById(userEntityOwner.getId())).thenReturn(Optional.of(userEntityOwner));
        when(bookingRepository.findPastByOwnerItems(userEntityOwner, Timestamp.valueOf(LocalDateTime.now()), Pageable.unpaged())).thenReturn(bookingEntities);
        when(bookingMapper.toBookingDtoList(bookingEntities)).thenReturn(Collections.singletonList(bookingDto));

        List<BookingDto> bookingDtos = bookingService.findPastByOwnerItems(userEntityOwner.getId(), Pageable.unpaged());

        assertNotNull(bookingDtos);
    }

    @Test
    void findCurrentByBooker_Successful() {
        List<BookingEntity> bookingEntities = Collections.singletonList(bookingEntity);

        when(userRepository.findById(userEntityRequestor.getId())).thenReturn(Optional.of(userEntityRequestor));
        when(bookingRepository.findCurrentByBooker(userEntityRequestor, Timestamp.valueOf(LocalDateTime.now()), Pageable.unpaged())).thenReturn(bookingEntities);
        when(bookingMapper.toBookingDtoList(bookingEntities)).thenReturn(Collections.singletonList(bookingDto));

        List<BookingDto> bookingDtos = bookingService.findCurrentByBooker(userEntityRequestor.getId(), Pageable.unpaged());

        assertNotNull(bookingDtos);
    }

    @Test
    void findPastByBooker_Successful() {
        List<BookingEntity> bookingEntities = Collections.singletonList(bookingEntity);

        when(userRepository.findById(userEntityRequestor.getId())).thenReturn(Optional.of(userEntityRequestor));
        when(bookingRepository.findPastByBooker(userEntityRequestor, Timestamp.valueOf(LocalDateTime.now()), Pageable.unpaged())).thenReturn(bookingEntities);
        when(bookingMapper.toBookingDtoList(bookingEntities)).thenReturn(Collections.singletonList(bookingDto));

        List<BookingDto> bookingDtos = bookingService.findPastByBooker(userEntityRequestor.getId(), Pageable.unpaged());

        assertNotNull(bookingDtos);
    }

    @Test
    void getAllUserBookingsByState_Successful() {
        List<BookingEntity> bookingEntities = Collections.singletonList(bookingEntity);

        when(userRepository.findById(userEntityRequestor.getId())).thenReturn(Optional.of(userEntityRequestor));
        when(bookingRepository.findAllByBookerIdAndStatusSortedByIdDesc(userEntityRequestor.getId(), BookingStatus.WAITING, Pageable.unpaged())).thenReturn(bookingEntities);
        when(bookingMapper.toBookingDtoList(bookingEntities)).thenReturn(Collections.singletonList(bookingDto));

        List<BookingDto> bookingDtos = bookingService.getAllUserBookingsByState(userEntityRequestor.getId(), "WAITING",Pageable.unpaged());

        assertNotNull(bookingDtos);
        assertFalse(bookingDtos.isEmpty());
    }

    @Test
    void updateBooking_StatusApproved_Successful() {
        Long bookingId = 1L;
        when(bookingRepository.findByIdWithItemAndBooker(bookingId)).thenReturn(Optional.of(bookingEntity));
        when(bookingRepository.save(bookingEntity)).thenReturn(bookingEntity);
        when(bookingMapper.toDto(bookingEntity)).thenReturn(bookingDto);

        BookingDto updatedBookingDto = bookingService.updateBooking(bookingId, userEntityOwner.getId(), true);

        assertNotNull(updatedBookingDto);
        assertEquals(bookingDto, updatedBookingDto);
    }

    @Test
    void getUserBookings_OrderByIdDesc_Successful() {
        Long userId = 1L;
        BookingStatus status = BookingStatus.ALL;
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "id"));

        List<BookingEntity> bookingEntities = Collections.singletonList(bookingEntity);

        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntityOwner));
        when(bookingRepository.findAllByBookerIdOrderByIdDesc(userId, pageable)).thenReturn(bookingEntities);
        when(bookingMapper.toBookingDtoList(bookingEntities)).thenReturn(Collections.singletonList(bookingDto));

        List<BookingDto> bookingDtos = bookingService.getUserBookings(userId, status, pageable);

        assertNotNull(bookingDtos);
        assertFalse(bookingDtos.isEmpty());
        assertEquals(1, bookingDtos.size());
    }

    @Test
    void getUserBookings_OrderByIdDesc_NoBookings() {
        Long userId = 1L;
        BookingStatus status = BookingStatus.ALL;
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "id"));

        List<BookingEntity> bookingEntities = Collections.emptyList();

        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntityOwner));
        when(bookingRepository.findAllByBookerIdOrderByIdDesc(userId, pageable)).thenReturn(bookingEntities);

        List<BookingDto> bookingDtos = bookingService.getUserBookings(userId, status, pageable);

        assertNotNull(bookingDtos);
        assertTrue(bookingDtos.isEmpty());
    }

    @Test
    void getOwnerBookingsByState_Current_Successful() {
        Long userId = 1L;
        String state = "CURRENT";
        Pageable pageable = PageRequest.of(0, 10);

        when(userRepository.findById(userId)).thenReturn(Optional.of(new UserEntity()));
        when(bookingRepository.findCurrentByOwnerItems(any(UserEntity.class), any(Timestamp.class), eq(pageable))).thenReturn(Collections.emptyList());

        List<BookingDto> bookingDtos = bookingService.getOwnerBookingsByState(userId, state, pageable);

        assertNotNull(bookingDtos);
        assertTrue(bookingDtos.isEmpty());
    }

    @Test
    void getOwnerBookingsByState_Past_Successful() {
        Long userId = 1L;
        String state = "PAST";
        Pageable pageable = PageRequest.of(0, 10);

        when(userRepository.findById(userId)).thenReturn(Optional.of(new UserEntity()));
        when(bookingRepository.findPastByOwnerItems(any(UserEntity.class), any(Timestamp.class), eq(pageable))).thenReturn(Collections.emptyList());

        List<BookingDto> bookingDtos = bookingService.getOwnerBookingsByState(userId, state, pageable);

        assertNotNull(bookingDtos);
        assertTrue(bookingDtos.isEmpty());
    }

    @Test
    void getAllUserBookingsByState_Current_Successful() {
        Long userId = 1L;
        String state = "CURRENT";
        Pageable pageable = PageRequest.of(0, 10);

        when(userRepository.findById(userId)).thenReturn(Optional.of(new UserEntity()));
        when(bookingRepository.findCurrentByBooker(any(UserEntity.class), any(Timestamp.class), eq(pageable))).thenReturn(Collections.emptyList());

        List<BookingDto> bookingDtos = bookingService.getAllUserBookingsByState(userId, state, pageable);

        assertNotNull(bookingDtos);
        assertTrue(bookingDtos.isEmpty());
    }

    @Test
    void getAllUserBookingsByState_Past_Successful() {
        Long userId = 1L;
        String state = "PAST";
        Pageable pageable = PageRequest.of(0, 10);

        when(userRepository.findById(userId)).thenReturn(Optional.of(new UserEntity()));
        when(bookingRepository.findPastByBooker(any(UserEntity.class), any(Timestamp.class), eq(pageable))).thenReturn(Collections.emptyList());

        List<BookingDto> bookingDtos = bookingService.getAllUserBookingsByState(userId, state, pageable);

        assertNotNull(bookingDtos);
        assertTrue(bookingDtos.isEmpty());
    }

    @Test
    void getOwnerBookingsByState_All_Successful() {
        Long userId = 1L;
        String state = "ALL";
        Pageable pageable = PageRequest.of(0, 10);

        when(userRepository.findById(userId)).thenReturn(Optional.of(new UserEntity()));
        when(bookingRepository.findAllByOwnerItemsAndStatus(any(UserEntity.class), eq(BookingStatus.ALL), eq(pageable))).thenReturn(Collections.emptyList());

        List<BookingDto> bookingDtos = bookingService.getOwnerBookingsByState(userId, state, pageable);

        assertNotNull(bookingDtos);
        assertTrue(bookingDtos.isEmpty());
    }

    @Test
    void getOwnerBookingsByState_Future_Successful() {
        Long userId = 1L;
        String state = "FUTURE";
        Pageable pageable = PageRequest.of(0, 10);

        when(userRepository.findById(userId)).thenReturn(Optional.of(new UserEntity()));
        when(bookingRepository.findAllByOwnerItemsAndStatus(any(UserEntity.class), eq(BookingStatus.FUTURE), eq(pageable))).thenReturn(Collections.emptyList());

        List<BookingDto> bookingDtos = bookingService.getOwnerBookingsByState(userId, state, pageable);

        assertNotNull(bookingDtos);
        assertTrue(bookingDtos.isEmpty());
    }

    @Test
    void getOwnerBookingsByState_Rejected_Successful() {
        Long userId = 1L;
        String state = "REJECTED";
        Pageable pageable = PageRequest.of(0, 10);

        when(userRepository.findById(userId)).thenReturn(Optional.of(new UserEntity()));
        when(bookingRepository.findAllByOwnerItemsAndStatus(any(UserEntity.class), eq(BookingStatus.REJECTED), eq(pageable))).thenReturn(Collections.emptyList());

        List<BookingDto> bookingDtos = bookingService.getOwnerBookingsByState(userId, state, pageable);

        assertNotNull(bookingDtos);
        assertTrue(bookingDtos.isEmpty());
    }

    @Test
    void getOwnerBookingsByState_Canceled_Successful() {
        Long userId = 1L;
        String state = "CANCELED";
        Pageable pageable = PageRequest.of(0, 10);

        when(userRepository.findById(userId)).thenReturn(Optional.of(new UserEntity()));
        when(bookingRepository.findAllByOwnerItemsAndStatus(any(UserEntity.class), eq(BookingStatus.CANCELED), eq(pageable))).thenReturn(Collections.emptyList());

        List<BookingDto> bookingDtos = bookingService.getOwnerBookingsByState(userId, state, pageable);

        assertNotNull(bookingDtos);
        assertTrue(bookingDtos.isEmpty());
    }

    @Test
    void getAllUserBookingsByState_All_Successful() {
        Long userId = 1L;
        String state = "ALL";
        Pageable pageable = PageRequest.of(0, 10);
        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntityOwner));
        when(bookingRepository.findAllByBookerIdOrderByIdDesc(eq(userId), eq(pageable))).thenReturn(Collections.emptyList());

        List<BookingDto> bookingDtos = bookingService.getAllUserBookingsByState(userId, state, pageable);

        assertNotNull(bookingDtos);
        assertTrue(bookingDtos.isEmpty());
    }

    @Test
    void getAllUserBookingsByState_Future_Successful() {
        Long userId = 1L;
        String state = "FUTURE";
        Pageable pageable = PageRequest.of(0, 10);
        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntityOwner));
        when(bookingRepository.findAllByBookerIdAndStatusSortedByIdDesc(eq(userId), eq(BookingStatus.FUTURE), eq(pageable))).thenReturn(Collections.emptyList());

        List<BookingDto> bookingDtos = bookingService.getAllUserBookingsByState(userId, state, pageable);

        assertNotNull(bookingDtos);
        assertTrue(bookingDtos.isEmpty());
    }

    @Test
    void getAllUserBookingsByState_Rejected_Successful() {
        Long userId = 1L;
        String state = "REJECTED";
        Pageable pageable = PageRequest.of(0, 10);
        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntityOwner));
        when(bookingRepository.findAllByBookerIdAndStatusSortedByIdDesc(eq(userId), eq(BookingStatus.REJECTED), eq(pageable))).thenReturn(Collections.emptyList());

        List<BookingDto> bookingDtos = bookingService.getAllUserBookingsByState(userId, state, pageable);

        assertNotNull(bookingDtos);
        assertTrue(bookingDtos.isEmpty());
    }

    @Test
    void getAllUserBookingsByState_Canceled_Successful() {
        Long userId = 1L;
        String state = "CANCELED";
        Pageable pageable = PageRequest.of(0, 10);
        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntityOwner));
        when(bookingRepository.findAllByBookerIdAndStatusSortedByIdDesc(eq(userId), eq(BookingStatus.CANCELED), eq(pageable))).thenReturn(Collections.emptyList());

        List<BookingDto> bookingDtos = bookingService.getAllUserBookingsByState(userId, state, pageable);

        assertNotNull(bookingDtos);
        assertTrue(bookingDtos.isEmpty());
    }
}
