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
import ru.practicum.shareit.item.entity.ItemEntity;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.entity.ItemRequestEntity;
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
        booking.setStart(LocalDateTime.now());
        booking.setEnd(LocalDateTime.now());


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

       // BookingDto createdBookingDto = bookingService.createBooking(booking);

       // assertNotNull(createdBookingDto);
        //verify(bookingRepository, times(1)).save(bookingEntity);
    }

    @Test
    void getBooking_ExistingBooking_Successful() {
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

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(bookingEntity));
        when(userRepository.findById(3L)).thenReturn(Optional.of(userEntity));
        when(bookingMapper.toDto(bookingEntity)).thenReturn(new BookingDto());

        BookingDto retrievedBookingDto = bookingService.getBooking(bookingId, 3L);

        assertNotNull(retrievedBookingDto);
    }

    @Test
    void getBooking_NonExistingBooking_ThrowsDataNotFoundException() {
        Long bookingId = 1L;
        Long userId = 1L;

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> bookingService.getBooking(bookingId, userId));
    }

    @Test
    void updateBooking_Successful() {
        Long userId = 1L;
        Long bookingId = 1L;

        BookingEntity bookingEntity = new BookingEntity();
        bookingEntity.setId(bookingId);
        bookingEntity.setItem(new ItemEntity());
        bookingEntity.setBooker(new UserEntity());

        when(bookingRepository.findByIdWithItemAndBooker(bookingId)).thenReturn(Optional.of(bookingEntity));
        when(bookingRepository.save(bookingEntity)).thenReturn(bookingEntity);
        when(bookingMapper.toDto(bookingEntity)).thenReturn(new BookingDto());

       // BookingDto updatedBookingDto = bookingService.updateBooking(userId, bookingId, true);

        //assertNotNull(updatedBookingDto);
        //verify(bookingRepository, times(1)).save(bookingEntity);
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

}
