package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.entity.BookingEntity;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.entity.UserEntity;

import java.util.List;

public interface BookingService {
    BookingDto createBooking(Booking booking);

    BookingDto getBooking(Long bookingId, Long userId);

    List<BookingDto> getUserBookings(Long userId, BookingStatus status);

    BookingDto updateBooking(Long userId, Long bookingId, Boolean approved);

    void deleteBooking(Long bookingId);

    List<BookingDto> findAllByOwnerItemsAndStatus(Long userId, BookingStatus status);

    List<BookingDto> findAllByOwner(Long userId);
}
