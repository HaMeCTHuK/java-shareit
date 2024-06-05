package ru.practicum.shareit.booking;

import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public interface BookingService {
    BookingDto createBooking(Booking booking);

    BookingDto getBooking(Long bookingId, Long userId);

    List<BookingDto> getUserBookings(Long userId, BookingStatus status, Pageable pageable);

    BookingDto updateBooking(Long userId, Long bookingId, Boolean approved);

    void deleteBooking(Long bookingId);

    List<BookingDto> findAllByOwnerItemsAndStatus(Long userId, BookingStatus status, Pageable pageable);

    List<BookingDto> findAllByOwner(Long userId, Pageable pageable);

    List<BookingDto> findCurrentByOwnerItems(Long userId, Pageable pageable);

    List<BookingDto> findPastByOwnerItems(Long userId, Pageable pageable);

    List<BookingDto> findCurrentByBooker(Long userId, Pageable pageable);

    List<BookingDto> findPastByBooker(Long userId, Pageable pageable);

    List<BookingDto> getOwnerBookingsByState(Long userId, String state,  Pageable pageable);

    List<BookingDto> getAllUserBookingsByState(Long userId, String state, Pageable pageable);
}
