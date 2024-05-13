package ru.practicum.shareit.booking.mapper;

import static org.junit.jupiter.api.Assertions.*;

import org.antlr.v4.runtime.misc.Array2DHashSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoCreate;
import ru.practicum.shareit.booking.entity.BookingEntity;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.entity.CommentEntity;
import ru.practicum.shareit.item.entity.ItemEntity;
import ru.practicum.shareit.user.entity.UserEntity;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BookingMapperTest {

    private final BookingMapper bookingMapper = new BookingMapperImpl();
    private BookingEntity bookingEntity;

    @BeforeEach
    void setUp() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setEmail("test@mail.com");
        userEntity.setName("Lesha");

        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setId(1L);
        commentEntity.setText("tratatat");
        commentEntity.setAuthor(userEntity);
        commentEntity.setCreated(Timestamp.valueOf(LocalDateTime.now()));
        commentEntity.setItem(new ItemEntity());

        Set <CommentEntity> commentEntitySet = new HashSet<>();
        commentEntitySet.add(commentEntity);

        UserEntity userEntity2 = new UserEntity();
        userEntity2.setId(1L);
        userEntity2.setEmail("test2@mail.com");
        userEntity2.setName("Lesha2");
        userEntity2.setComments(commentEntitySet);

        ItemEntity itemEntity = new ItemEntity();
        itemEntity.setId(1L);
        itemEntity.setOwner(userEntity2);
        itemEntity.setAvailable(true);
        itemEntity.setDescription("trtrtr");

        bookingEntity = new BookingEntity();
        bookingEntity.setId(1L);
        bookingEntity.setStart(Timestamp.valueOf(LocalDateTime.now()));
        bookingEntity.setEnd(Timestamp.valueOf(LocalDateTime.now().plusHours(1)));
        bookingEntity.setItem(itemEntity);
        bookingEntity.setStatus(BookingStatus.APPROVED);
        bookingEntity.setBooker(userEntity2);
    }

    @Test
    void testToBooking() {
        Booking booking = bookingMapper.toBooking(bookingEntity);

        assertNotNull(booking);
        assertEquals(bookingEntity.getStart().toLocalDateTime(), booking.getStart());
        assertEquals(bookingEntity.getEnd().toLocalDateTime(), booking.getEnd());
    }

    @Test
    void testToBookingFromBookingDtoCreate() {
        BookingDtoCreate bookingDtoCreate = new BookingDtoCreate();
        bookingDtoCreate.setItemId(1L);

        Booking booking = bookingMapper.toBookingFromBookingDtoCreate(bookingDtoCreate, 2L);

        assertNotNull(booking);
        assertEquals(2L, booking.getBooker().getId());
        assertEquals(1L, booking.getItem().getId());
    }

    @Test
    void testToEntity() {
        Booking booking = new Booking();
        booking.setStart(LocalDateTime.now());
        booking.setEnd(LocalDateTime.now().plusHours(1));

        BookingEntity bookingEntity = bookingMapper.toEntity(booking);

        assertNotNull(bookingEntity);
        assertEquals(Timestamp.valueOf(booking.getStart()), bookingEntity.getStart());
        assertEquals(Timestamp.valueOf(booking.getEnd()), bookingEntity.getEnd());
    }

    @Test
    void testToDto() {
        BookingEntity savedBooking = new BookingEntity();
        savedBooking.setStart(Timestamp.valueOf(LocalDateTime.now()));
        savedBooking.setEnd(Timestamp.valueOf(LocalDateTime.now().plusHours(1)));

        BookingDto bookingDto = bookingMapper.toDto(savedBooking);

        assertNotNull(bookingDto);
        assertEquals(savedBooking.getStart().toLocalDateTime(), bookingDto.getStart());
        assertEquals(savedBooking.getEnd().toLocalDateTime(), bookingDto.getEnd());
    }

    @Test
    void testToLocalDateTime() {
        Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());

        LocalDateTime localDateTime = bookingMapper.toLocalDateTime(timestamp);

        assertNotNull(localDateTime);
        assertEquals(timestamp.toLocalDateTime(), localDateTime);
    }

    @Test
    void testToTimeStamp() {
        LocalDateTime localDateTime = LocalDateTime.now();

        Timestamp timestamp = bookingMapper.toTimeStamp(localDateTime);

        assertNotNull(timestamp);
        assertEquals(Timestamp.valueOf(localDateTime), timestamp);
    }

    @Test
    void testToBookingDtoList() {
        BookingEntity bookingEntity1 = new BookingEntity();
        bookingEntity1.setStart(Timestamp.valueOf(LocalDateTime.now()));
        bookingEntity1.setEnd(Timestamp.valueOf(LocalDateTime.now().plusHours(1)));

        BookingEntity bookingEntity2 = new BookingEntity();
        bookingEntity2.setStart(Timestamp.valueOf(LocalDateTime.now().plusDays(1)));
        bookingEntity2.setEnd(Timestamp.valueOf(LocalDateTime.now().plusDays(1).plusHours(1)));

        List<BookingEntity> bookingEntityList = new ArrayList<>();
        bookingEntityList.add(bookingEntity1);
        bookingEntityList.add(bookingEntity2);

        List<BookingDto> bookingDtoList = bookingMapper.toBookingDtoList(bookingEntityList);

        assertNotNull(bookingDtoList);
        assertEquals(2, bookingDtoList.size());
        assertEquals(bookingEntity1.getStart().toLocalDateTime(), bookingDtoList.get(0).getStart());
        assertEquals(bookingEntity1.getEnd().toLocalDateTime(), bookingDtoList.get(0).getEnd());
        assertEquals(bookingEntity2.getStart().toLocalDateTime(), bookingDtoList.get(1).getStart());
        assertEquals(bookingEntity2.getEnd().toLocalDateTime(), bookingDtoList.get(1).getEnd());
    }
}
