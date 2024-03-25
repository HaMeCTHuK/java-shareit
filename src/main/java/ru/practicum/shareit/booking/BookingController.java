package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-bookings.
 */
@Slf4j
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

    @Autowired
    private final BookingService bookingService;
    private final BookingMapper bookingMapper;

    @PostMapping
    public BookingDto createBooking(@RequestHeader("X-Sharer-User-Id") Long userId, @Valid @RequestBody BookingDto bookingDto) {
        Booking booking = bookingMapper.toBookingFromBookingDtoCreate(bookingDto, userId);
        log.info("Пытаемся создать booking: {}", booking);
        return bookingService.createBooking(booking);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBooking(@PathVariable Long bookingId) {
        log.info("Получаем объект бронирования по id: {}", bookingId);
        return bookingService.getBooking(bookingId);
    }

    @GetMapping("/user/{userId}")
    public List<BookingDto> getUserBookings(@PathVariable Long userId) {
        log.info("Получаем бронирования пользователя с id: {}", userId);
        return bookingService.getUserBookings(userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto updateBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                    @PathVariable Long bookingId, @RequestBody BookingDto bookingDto) {
        log.info("Пытаемся обновить бронирование с id: {}", bookingId);
        return bookingService.updateBooking(userId, bookingId, bookingDto);
    }

    @DeleteMapping("/{bookingId}")
    public void deleteBooking(@PathVariable Long bookingId) {
        log.info("Удаляем бронирование с id: {}", bookingId);
        bookingService.deleteBooking(bookingId);
    }
}
