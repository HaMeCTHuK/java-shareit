package ru.practicum.shareit.booking.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.entity.BookingEntity;
import ru.practicum.shareit.item.entity.ItemEntity;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.entity.ItemRequestEntity;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.entity.UserEntity;
import ru.practicum.shareit.user.repository.UserRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class BookingRepositoryTest {

    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRequestRepository itemRequestRepository;

    @Test
    public void testFindAllByItemId() {
        Long itemId = 1L;
        Timestamp now = Timestamp.valueOf(LocalDateTime.now());

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

        ItemEntity savedItem = itemRepository.save(itemEntity);

        BookingEntity bookingEntity = new BookingEntity();
        bookingEntity.setItem(savedItem);
        bookingEntity.setBooker(savedUserEntity);
        bookingEntity.setStart(now);
        bookingEntity.setEnd(now);
        bookingEntity.setStatus(BookingStatus.APPROVED);

        bookingRepository.save(bookingEntity);

        List<BookingEntity> bookings = bookingRepository.findAllByItemId(itemId);
        assertNotNull(bookings);
        assertFalse(bookings.isEmpty());

    }

    @Test
    public void testFindFutureByBooker() {
        Timestamp now = Timestamp.valueOf(LocalDateTime.now());

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

        ItemEntity savedItem = itemRepository.save(itemEntity);

        BookingEntity bookingEntity = new BookingEntity();
        bookingEntity.setItem(savedItem);
        bookingEntity.setBooker(savedUserEntity);
        bookingEntity.setStart(Timestamp.valueOf(LocalDateTime.now().plusDays(1)));
        bookingEntity.setEnd(Timestamp.valueOf(LocalDateTime.now().plusDays(2)));
        bookingEntity.setStatus(BookingStatus.APPROVED);

        bookingRepository.save(bookingEntity);

        List<BookingEntity> futureBookings = bookingRepository.findFutureByBooker(savedUserEntity, now, PageRequest.of(0, 10));

        assertNotNull(futureBookings);
        assertEquals(1, futureBookings.size());

    }

    @Test
    void testFindAllByItemAndBooker() {
        Timestamp now = Timestamp.valueOf(LocalDateTime.now());

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

        ItemEntity savedItem = itemRepository.save(itemEntity);

        BookingEntity bookingEntity = new BookingEntity();
        bookingEntity.setItem(savedItem);
        bookingEntity.setBooker(savedUserEntity);
        bookingEntity.setStart(now);
        bookingEntity.setEnd(now);
        bookingEntity.setStatus(BookingStatus.APPROVED);

        bookingRepository.save(bookingEntity);

        List<BookingEntity> bookings = bookingRepository.findAllByItemAndBooker(savedItem, savedUserEntity);
        assertEquals(1, bookings.size());
        assertEquals(savedUserEntity.getId(), bookings.get(0).getBooker().getId());
    }

    @Test
    void testFindAllByItemOwnerOrderByIdDesc() {
        UserEntity owner = new UserEntity();
        owner.setName("Owner");
        owner.setEmail("owner@123.com");
        UserEntity savedOwner = userRepository.save(owner);

        ItemEntity item1 = new ItemEntity();
        item1.setName("Test1");
        item1.setDescription("Test1");
        item1.setAvailable(true);
        item1.setOwner(savedOwner);
        ItemEntity savedItem1 = itemRepository.save(item1);

        ItemEntity item2 = new ItemEntity();
        item2.setName("Test2");
        item2.setDescription("Test2");
        item2.setAvailable(true);
        item2.setOwner(savedOwner);
        ItemEntity savedItem2 = itemRepository.save(item2);

        BookingEntity booking1 = new BookingEntity();
        booking1.setBooker(savedOwner);
        booking1.setItem(savedItem1);
        booking1.setStart(Timestamp.valueOf(LocalDateTime.now()));
        booking1.setEnd(Timestamp.valueOf(LocalDateTime.now().plusDays(1)));
        booking1.setStatus(BookingStatus.APPROVED);
        BookingEntity savedBooking1 = bookingRepository.save(booking1);

        BookingEntity booking2 = new BookingEntity();
        booking2.setBooker(savedOwner);
        booking2.setItem(savedItem2);
        booking2.setStart(Timestamp.valueOf(LocalDateTime.now()));
        booking2.setEnd(Timestamp.valueOf(LocalDateTime.now().plusDays(1)));
        booking2.setStatus(BookingStatus.APPROVED);
        BookingEntity savedBooking2 = bookingRepository.save(booking2);

        List<BookingEntity> bookings = bookingRepository.findAllByItemOwnerOrderByIdDesc(savedOwner, PageRequest.of(0, 10));
        assertEquals(2, bookings.size());
        assertEquals(savedBooking2.getId(), bookings.get(0).getId());
        assertEquals(savedBooking1.getId(), bookings.get(1).getId());
    }

}
