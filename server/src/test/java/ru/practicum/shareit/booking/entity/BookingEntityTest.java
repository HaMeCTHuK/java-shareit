package ru.practicum.shareit.booking.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.entity.ItemEntity;
import ru.practicum.shareit.user.entity.UserEntity;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class BookingEntityTest {

    @Mock
    private ItemEntity mockedItemEntity;

    @Mock
    private UserEntity mockedUserEntity;

    @Test
    public void testGettersAndSetters() {
        Timestamp start = new Timestamp(System.currentTimeMillis());
        Timestamp end = new Timestamp(System.currentTimeMillis() + 3600000);
        BookingStatus status = BookingStatus.APPROVED;

        BookingEntity booking = new BookingEntity();
        booking.setId(1L);
        booking.setStart(start);
        booking.setEnd(end);
        booking.setItem(mockedItemEntity);
        booking.setBooker(mockedUserEntity);
        booking.setStatus(status);

        assertEquals(1L, booking.getId());
        assertEquals(start, booking.getStart());
        assertEquals(end, booking.getEnd());
        assertEquals(mockedItemEntity, booking.getItem());
        assertEquals(mockedUserEntity, booking.getBooker());
        assertEquals(status, booking.getStatus());
    }

    @Test
    public void testNoArgsConstructor() {
        BookingEntity booking = new BookingEntity();
        assertNotNull(booking);
    }

    @Test
    public void testAllArgsConstructor() {
        Timestamp start = new Timestamp(System.currentTimeMillis());
        Timestamp end = new Timestamp(System.currentTimeMillis() + 3600000);
        BookingStatus status = BookingStatus.APPROVED;

        BookingEntity booking = new BookingEntity();
        booking.setId(1L);
        booking.setStart(start);
        booking.setEnd(end);
        booking.setItem(mockedItemEntity);
        booking.setBooker(mockedUserEntity);
        booking.setStatus(status);

        assertEquals(1L, booking.getId());
        assertEquals(start, booking.getStart());
        assertEquals(end, booking.getEnd());
        assertEquals(mockedItemEntity, booking.getItem());
        assertEquals(mockedUserEntity, booking.getBooker());
        assertEquals(status, booking.getStatus());
    }
}
