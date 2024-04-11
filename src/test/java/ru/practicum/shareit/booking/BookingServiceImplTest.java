package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
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

    @Test
    void createBooking_Successful() {
        Booking booking = new Booking();
        booking.setStart(LocalDateTime.now().plusDays(1));
        booking.setEnd(LocalDateTime.now().plusDays(2));

        Item item = new Item();
        item.setId(1L);
        item.setAvailable(true);
        booking.setItem(item);

        User user = new User();
        user.setId(4L);
        booking.setBooker(user);

        UserEntity userEntityOwner = new UserEntity();
        userEntityOwner.setId(3L);
        userEntityOwner.setEmail("123@123.ru");
        userEntityOwner.setName("boss");

        UserEntity userEntityRequestor = new UserEntity();
        userEntityRequestor.setId(4L);
        userEntityRequestor.setEmail("123@123.ru");
        userEntityRequestor.setName("boss");

        ItemRequestEntity itemRequestEntity = new ItemRequestEntity();
        itemRequestEntity.setId(2L);
        itemRequestEntity.setCreated(Timestamp.valueOf(LocalDateTime.now()));
        itemRequestEntity.setRequestor(userEntityRequestor);
        itemRequestEntity.setDescription("asdasfas");

        ItemEntity itemEntity = new ItemEntity();
        itemEntity.setId(1L);
        itemEntity.setOwner(userEntityOwner);
        itemEntity.setName("123123");
        itemEntity.setDescription("1231241241");
        itemEntity.setAvailable(true);
        itemEntity.setRequest(itemRequestEntity);


        BookingEntity bookingEntity = new BookingEntity();
        BookingDto bookingDto = new BookingDto();
        bookingDto.setStart(LocalDateTime.now());
        bookingDto.setEnd(LocalDateTime.now());

        when(itemRepository.findById(1L)).thenReturn(Optional.of(itemEntity));
        when(userRepository.findById(4L)).thenReturn(Optional.of(userEntityRequestor));
        when(userRepository.findById(3L)).thenReturn(Optional.of(userEntityOwner));
        when(bookingMapper.toEntity(booking)).thenReturn(bookingEntity);
        when(bookingRepository.save(bookingEntity)).thenReturn(bookingEntity);
        when(bookingMapper.toDto(bookingEntity)).thenReturn(bookingDto);

        BookingDto createdBookingDto = bookingService.createBooking(booking);

        assertNotNull(createdBookingDto);
        verify(bookingRepository, times(1)).save(bookingEntity);
    }

    @Test
    void getBooking_Successful() {
        Long bookingId = 1L;

        UserEntity userEntity = new UserEntity();
        userEntity.setId(3L);
        userEntity.setEmail("123@123.ru");
        userEntity.setName("boss");

        ItemRequestEntity itemRequestEntity = new ItemRequestEntity();
        itemRequestEntity.setId(2L);
        itemRequestEntity.setCreated(Timestamp.valueOf(LocalDateTime.now()));
        itemRequestEntity.setRequestor(userEntity);
        itemRequestEntity.setDescription("asdasfas");

        ItemEntity itemEntity = new ItemEntity();
        itemEntity.setId(1L);
        itemEntity.setOwner(userEntity);
        itemEntity.setName("123123");
        itemEntity.setDescription("1231241241");
        itemEntity.setAvailable(true);
        itemEntity.setRequest(itemRequestEntity);

        BookingEntity bookingEntity = new BookingEntity();
        bookingEntity.setId(bookingId);
        bookingEntity.setItem(itemEntity);
        bookingEntity.setBooker(userEntity);
        bookingEntity.setStatus(BookingStatus.APPROVED);

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(bookingEntity));
        when(userRepository.findById(3L)).thenReturn(Optional.of(userEntity));
        when(bookingMapper.toDto(bookingEntity)).thenReturn(new BookingDto());

        BookingDto retrievedBookingDto = bookingService.getBooking(bookingId, 3L);

        assertNotNull(retrievedBookingDto);
    }

    @Test
    void getBooking_DataNotFoundException() {
        Long bookingId = 1L;
        Long userId = 1L;

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> bookingService.getBooking(bookingId, userId));
    }

    @Test
    void updateBooking_Successful() {
        Long userId = 1L;
        Long bookingId = 1L;

        UserEntity userEntityOwner = new UserEntity();
        userEntityOwner.setId(1L);
        userEntityOwner.setEmail("123@123.ru");
        userEntityOwner.setName("boss");

        UserEntity userEntityRequestor = new UserEntity();
        userEntityRequestor.setId(1L);
        userEntityRequestor.setEmail("123@123.ru");
        userEntityRequestor.setName("boss");

        ItemRequestEntity itemRequestEntity = new ItemRequestEntity();
        itemRequestEntity.setId(2L);
        itemRequestEntity.setCreated(Timestamp.valueOf(LocalDateTime.now()));
        itemRequestEntity.setRequestor(userEntityRequestor);
        itemRequestEntity.setDescription("asdasfas");

        ItemEntity itemEntity = new ItemEntity();
        itemEntity.setId(1L);
        itemEntity.setOwner(userEntityOwner);
        itemEntity.setName("123123");
        itemEntity.setDescription("1231241241");
        itemEntity.setAvailable(true);
        itemEntity.setRequest(itemRequestEntity);

        BookingEntity bookingEntity = new BookingEntity();
        bookingEntity.setId(bookingId);
        bookingEntity.setItem(itemEntity);
        bookingEntity.setBooker(userEntityRequestor);
        bookingEntity.setStatus(BookingStatus.CANCELED);

        BookingDto bookingDto = new BookingDto();
        bookingDto.setStart(LocalDateTime.now().plusDays(1));
        bookingDto.setEnd(LocalDateTime.now().plusDays(2));
        bookingDto.setItem(new ItemDto());
        bookingDto.setBooker(new UserDto());
        bookingDto.setStatus(BookingStatus.CANCELED);

        when(bookingRepository.findByIdWithItemAndBooker(bookingId)).thenReturn(Optional.of(bookingEntity));
        when(bookingRepository.save(bookingEntity)).thenReturn(bookingEntity);
        when(bookingMapper.toDto(bookingEntity)).thenReturn(bookingDto);

       BookingDto updatedBookingDto = bookingService.updateBooking(userId, bookingId, true);

        assertNotNull(updatedBookingDto);
        verify(bookingRepository, times(1)).save(bookingEntity);
    }

    @Test
    void deleteBooking_Successful() {
        Long bookingId = 1L;

        bookingService.deleteBooking(bookingId);

        verify(bookingRepository, times(1)).deleteById(bookingId);
    }

    @Test
    void findAllByOwnerItemsAndStatus_Successful() {
        Long userId = 1L;
        BookingStatus status = BookingStatus.ALL;
        Pageable pageable = Pageable.unpaged();

        UserEntity owner = new UserEntity();
        owner.setId(userId);

        List<BookingEntity> bookingEntities = Collections.singletonList(new BookingEntity());

        when(userRepository.findById(userId)).thenReturn(Optional.of(owner));
        when(bookingRepository.findAllByOwnerItemsAndStatus(owner, status, pageable)).thenReturn(bookingEntities);
        when(bookingMapper.toBookingDtoList(bookingEntities)).thenReturn(Collections.singletonList(new BookingDto()));

        List<BookingDto> bookingDtos = bookingService.findAllByOwnerItemsAndStatus(userId, status, pageable);

        assertNotNull(bookingDtos);
        assertTrue(bookingDtos.isEmpty());
    }

    @Test
    void getUserBookings_Successful() {
        Long userId = 1L;
        BookingStatus status = BookingStatus.FUTURE;
        Pageable pageable = Pageable.unpaged();

        UserEntity userEntity = new UserEntity();
        userEntity.setId(userId);
        userEntity.setEmail("test@test.com");
        userEntity.setName("Jo");

        UserDto userDto = new UserDto();
        userDto.setId(userId);
        userDto.setEmail("test@t.com");
        userDto.setName("Jonnny");

        BookingEntity bookingEntity = new BookingEntity();
        bookingEntity.setId(1L);
        bookingEntity.setItem(new ItemEntity());
        bookingEntity.setBooker(userEntity);

        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(1L);
        bookingDto.setItem(new ItemDto());
        bookingDto.setBooker(userDto);

        List<BookingEntity> bookingEntities = Collections.singletonList(bookingEntity);
        List<BookingDto> bookDtos = Collections.singletonList(bookingDto);

        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
        when(bookingRepository.findAllByBookerIdAndStatusSortedByIdDesc(userId, status, pageable)).thenReturn(bookingEntities);
        when(bookingMapper.toBookingDtoList(bookingEntities)).thenReturn(bookDtos);

        List<BookingDto> bookingDtos = bookingService.getUserBookings(userId, status, pageable);

        assertNotNull(bookingDtos);
        assertTrue(bookingDtos.isEmpty());
    }

    @Test
    void getOwnerBookingsByState_Successful() {
        Long userId = 1L;
        String state = "WAITING";
        Pageable pageable = Pageable.unpaged();

        UserEntity owner = new UserEntity();
        owner.setId(userId);

        List<BookingEntity> bookingEntities = Collections.singletonList(new BookingEntity());

        when(userRepository.findById(userId)).thenReturn(Optional.of(owner));
        when(bookingRepository.findAllByOwnerItemsAndStatus(owner, BookingStatus.WAITING, pageable)).thenReturn(bookingEntities);
        when(bookingMapper.toBookingDtoList(bookingEntities)).thenReturn(Collections.singletonList(new BookingDto()));

        List<BookingDto> bookingDtos = bookingService.getOwnerBookingsByState(userId, state, pageable);

        assertNotNull(bookingDtos);
        assertFalse(bookingDtos.isEmpty());
    }

    @Test
    void findAllByOwner_Successful() {
        Long userId = 1L;
        Pageable pageable = Pageable.unpaged();

        UserEntity owner = new UserEntity();
        owner.setId(userId);

        List<BookingEntity> bookingEntities = Collections.singletonList(new BookingEntity());

        when(userRepository.findById(userId)).thenReturn(Optional.of(owner));
        when(bookingRepository.findAllByOwnerItemsAndStatus(owner, BookingStatus.ALL, pageable)).thenReturn(bookingEntities);
        when(bookingMapper.toBookingDtoList(bookingEntities)).thenReturn(Collections.singletonList(new BookingDto()));

        List<BookingDto> bookingDtos = bookingService.findAllByOwner(userId, pageable);

        assertNotNull(bookingDtos);
        assertFalse(bookingDtos.isEmpty());
    }

    @Test
    void findCurrentByOwnerItems_Successful() {
        Long userId = 1L;
        Pageable pageable = Pageable.unpaged();

        UserEntity owner = new UserEntity();
        owner.setId(userId);

        List<BookingEntity> bookingEntities = Collections.singletonList(new BookingEntity());

        when(userRepository.findById(userId)).thenReturn(Optional.of(owner));
        when(bookingRepository.findCurrentByOwnerItems(owner, Timestamp.valueOf(LocalDateTime.now()), pageable)).thenReturn(bookingEntities);
        when(bookingMapper.toBookingDtoList(bookingEntities)).thenReturn(Collections.singletonList(new BookingDto()));

        List<BookingDto> bookingDtos = bookingService.findCurrentByOwnerItems(userId, pageable);

        assertNotNull(bookingDtos);
        assertTrue(bookingDtos.isEmpty());
    }

    @Test
    void findPastByOwnerItems_Successful() {
        Long userId = 1L;
        Pageable pageable = Pageable.unpaged();

        UserEntity owner = new UserEntity();
        owner.setId(userId);

        List<BookingEntity> bookingEntities = Collections.singletonList(new BookingEntity());

        when(userRepository.findById(userId)).thenReturn(Optional.of(owner));
        when(bookingRepository.findPastByOwnerItems(owner, Timestamp.valueOf(LocalDateTime.now()), pageable)).thenReturn(bookingEntities);
        when(bookingMapper.toBookingDtoList(bookingEntities)).thenReturn(Collections.singletonList(new BookingDto()));

        List<BookingDto> bookingDtos = bookingService.findPastByOwnerItems(userId, pageable);

        assertNotNull(bookingDtos);
        assertTrue(bookingDtos.isEmpty());
    }

    @Test
    void findCurrentByBooker_Successful() {
        Long userId = 1L;
        Pageable pageable = Pageable.unpaged();

        UserEntity owner = new UserEntity();
        owner.setId(userId);

        List<BookingEntity> bookingEntities = Collections.singletonList(new BookingEntity());

        when(userRepository.findById(userId)).thenReturn(Optional.of(owner));
        when(bookingRepository.findCurrentByBooker(owner, Timestamp.valueOf(LocalDateTime.now()), pageable)).thenReturn(bookingEntities);
        when(bookingMapper.toBookingDtoList(bookingEntities)).thenReturn(Collections.singletonList(new BookingDto()));

        List<BookingDto> bookingDtos = bookingService.findCurrentByBooker(userId, pageable);

        assertNotNull(bookingDtos);
        assertTrue(bookingDtos.isEmpty());
    }

    @Test
    void findPastByBooker_Successful() {
        Long userId = 1L;
        Pageable pageable = Pageable.unpaged();

        UserEntity owner = new UserEntity();
        owner.setId(userId);

        List<BookingEntity> bookingEntities = Collections.singletonList(new BookingEntity());

        when(userRepository.findById(userId)).thenReturn(Optional.of(owner));
        when(bookingRepository.findPastByBooker(owner, Timestamp.valueOf(LocalDateTime.now()), pageable)).thenReturn(bookingEntities);
        when(bookingMapper.toBookingDtoList(bookingEntities)).thenReturn(Collections.singletonList(new BookingDto()));

        List<BookingDto> bookingDtos = bookingService.findPastByBooker(userId, pageable);

        assertNotNull(bookingDtos);
        assertTrue(bookingDtos.isEmpty());
    }


}
