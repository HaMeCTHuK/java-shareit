package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoCreate;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.ValidationException;

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
    public BookingDto createBooking(@RequestHeader("X-Sharer-User-Id") Long userId, @Valid @RequestBody BookingDtoCreate bookingDtoCreate) {
        Booking booking = bookingMapper.toBookingFromBookingDtoCreate(bookingDtoCreate, userId);
        log.info("Пытаемся создать booking: {}", booking);
        return bookingService.createBooking(booking);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                 @PathVariable Long bookingId) {
        log.info("Получаем объект бронирования по id: {}", bookingId);
        return bookingService.getBooking(bookingId, userId);
    }

    @GetMapping("/owner")
    public List<BookingDto> getOwnerBookings(@RequestHeader("X-Sharer-User-Id") Long userId,
                                            @RequestParam(required = false, defaultValue = "ALL") String state) {
        log.info("Получаем бронирования пользователя с id: {}", userId);
        BookingStatus status;
        switch (state) {
        case "CURRENT":
            return bookingService.findCurrentByOwnerItems(userId);
        case "PAST":
            return bookingService.findPastByOwnerItems(userId);
        case "ALL":
            status = BookingStatus.ALL;
            return bookingService.findAllByOwnerItemsAndStatus(userId, status);
        case "FUTURE":
            status = BookingStatus.FUTURE;
            return bookingService.findAllByOwnerItemsAndStatus(userId, status);
        case "WAITING":
            status = BookingStatus.WAITING;
            return bookingService.findAllByOwnerItemsAndStatus(userId, status);
        case "REJECTED":
            status = BookingStatus.REJECTED;
            return bookingService.findAllByOwnerItemsAndStatus(userId, status);
        case "CANCELED":
            status = BookingStatus.CANCELED;
            return bookingService.findAllByOwnerItemsAndStatus(userId, status);
        default:
            throw new ValidationException("Unknown state: " + state);
        }
    }

    @GetMapping
    public List<BookingDto> getAllUserBookings(@RequestHeader("X-Sharer-User-Id") Long userId,
                                               @RequestParam(required = false, defaultValue = "ALL") String state) {
        log.info("Получаем все бронирования пользователя с id: {}", userId);
        BookingStatus status;
        switch (state) {
        case "CURRENT":
            return bookingService.findCurrentByBooker(userId);
        case "PAST":
            return bookingService.findPastByBooker(userId);
        case "ALL":
            status = BookingStatus.ALL;
            return bookingService.getUserBookings(userId, status);
        case "FUTURE":
            status = BookingStatus.FUTURE;
            return bookingService.getUserBookings(userId, status);
        case "WAITING":
            status = BookingStatus.WAITING;
            return bookingService.getUserBookings(userId, status);
        case "REJECTED":
            status = BookingStatus.REJECTED;
            return bookingService.getUserBookings(userId, status);
        case "CANCELED":
            status = BookingStatus.CANCELED;
            return bookingService.getUserBookings(userId, status);
        default:
            throw new ValidationException("Unknown state: " + state);
        }
    }

    @PatchMapping("/{bookingId}")
    public BookingDto updateBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                    @PathVariable Long bookingId,
                                    @RequestParam Boolean approved) {
        log.info("Пытаемся обновить бронирование с id: {}", bookingId);
        return bookingService.updateBooking(userId, bookingId, approved);
    }

    @DeleteMapping("/{bookingId}")
    public void deleteBooking(@PathVariable Long bookingId) {
        log.info("Удаляем бронирование с id: {}", bookingId);
        bookingService.deleteBooking(bookingId);
    }
}
