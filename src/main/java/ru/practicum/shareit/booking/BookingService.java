package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public interface BookingService {
    BookingDto createBooking(Booking booking);

    BookingDto getBooking(Long bookingId);

    List<BookingDto> getUserBookings(Long userId);

    BookingDto updateBooking(Long userId, Long bookingId, BookingDto bookingDto);

    void deleteBooking(Long bookingId);
}
