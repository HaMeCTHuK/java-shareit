package ru.practicum.shareit.booking.entity;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.practicum.shareit.booking.BookingStatus;

import java.sql.Timestamp;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class BookingEntityTest {

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void whenSaveBooking_thenBookingIsSaved() {
        BookingEntity booking = new BookingEntity();
        booking.setStart(new Timestamp(System.currentTimeMillis()));
        booking.setEnd(new Timestamp(System.currentTimeMillis() + 3600000));
        booking.setStatus(BookingStatus.APPROVED);

        BookingEntity savedBooking = entityManager.persistFlushFind(booking);

        assertThat(savedBooking).isNotNull();
        assertThat(savedBooking.getId()).isNotNull();

    }

    @Test
    public void whenFindBookingById_thenBookingIsFound() {
        BookingEntity booking = new BookingEntity();
        booking.setStart(new Timestamp(System.currentTimeMillis()));
        booking.setEnd(new Timestamp(System.currentTimeMillis() + 3600000));
        booking.setStatus(BookingStatus.APPROVED);

        BookingEntity savedBooking = entityManager.persistFlushFind(booking);
        BookingEntity foundBooking = entityManager.find(BookingEntity.class, savedBooking.getId());

        assertThat(foundBooking).isEqualTo(savedBooking);
    }

    @Test
    public void whenUpdateBooking_thenBookingIsUpdated() {
        BookingEntity booking = new BookingEntity();

        booking.setStart(new Timestamp(System.currentTimeMillis()));
        booking.setEnd(new Timestamp(System.currentTimeMillis() + 3600000)); // 1 hour from now
        booking.setStatus(BookingStatus.APPROVED);

        BookingEntity savedBooking = entityManager.persistFlushFind(booking);

        savedBooking.setStatus(BookingStatus.CANCELED);
        entityManager.persistAndFlush(savedBooking);

        BookingEntity updatedBooking = entityManager.find(BookingEntity.class, savedBooking.getId());

        assertThat(updatedBooking.getStatus()).isEqualTo(BookingStatus.CANCELED);
    }

    @Test
    public void whenDeleteBooking_thenBookingIsDeleted() {
        BookingEntity booking = new BookingEntity();
        booking.setStart(new Timestamp(System.currentTimeMillis()));
        booking.setEnd(new Timestamp(System.currentTimeMillis() + 3600000));
        booking.setStatus(BookingStatus.APPROVED);

        BookingEntity savedBooking = entityManager.persistFlushFind(booking);

        entityManager.remove(savedBooking);
        entityManager.flush();

        BookingEntity deletedBooking = entityManager.find(BookingEntity.class, savedBooking.getId());

        assertThat(deletedBooking).isNull();
    }
}
