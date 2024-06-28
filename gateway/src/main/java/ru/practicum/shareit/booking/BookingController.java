package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import static ru.practicum.shareit.common.Constants.X_SHARER_USER_ID;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final BookingClient bookingClient;

    @GetMapping
    public ResponseEntity<Object> getAllByBooker(@RequestHeader(X_SHARER_USER_ID) long userId,
                                              @RequestParam(name = "state", defaultValue = "all") String stateParam,
                                              @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                              @Positive @RequestParam(defaultValue = "10") Integer size) {
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
        log.info("Пришел /GET запрос на получение списка всех бронирований для пользователя с id {}, и с параметром {}",
                userId, state);
        ResponseEntity<Object> bookings = bookingClient.getAllByBooker(userId, state, from, size);
        log.info("Ответ отправлен {}", bookings);
        return bookings;
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getAllByOwner(@RequestHeader(X_SHARER_USER_ID) long userId,
                                                @RequestParam(name = "state", defaultValue = "all") String stateParam,
                                                @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                @Positive @RequestParam(defaultValue = "10") Integer size) {
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
        log.info("Пришел /GET запрос на получение списка всех бронирований для владельца с id {}, и с параметром {}",
                userId, state);
        ResponseEntity<Object> bookings = bookingClient.getAllByOwner(userId, state, from, size);
        log.info("Ответ отправлен {}", bookings);
        return bookings;
    }

        @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> approve(@PathVariable Long bookingId, @RequestHeader(X_SHARER_USER_ID) Long userId,
                                      @RequestParam Boolean approved) {
        log.info("Пришел /POST запрос на принятие или отклонение аренды от пользователя с id {} к предмету с id {}",
                userId, bookingId);
        ResponseEntity<Object> booking = bookingClient.approve(bookingId, userId, approved);
        log.info("Ответ отправлен {}", booking);
        return booking;
    }

    @PostMapping
    public ResponseEntity<Object> create(@Valid @RequestBody BookingRequestDto bookingDto,
                                           @RequestHeader(X_SHARER_USER_ID) Long userId) {
        log.info("Пришел POST запрос на добавление новой аренды {} от пользователя с id {}", bookingDto, userId);
        ResponseEntity<Object> booking = bookingClient.create(userId, bookingDto);
        log.info("Ответ отправлен {}", booking);
        return booking;
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getById(@RequestHeader(X_SHARER_USER_ID) long userId,
                                             @PathVariable Long bookingId) {
        log.info("Пришел /GET запрос на получение данных об аренде с id {} от пользователя {}", bookingId, userId);
        ResponseEntity<Object> booking = bookingClient.getById(userId, bookingId);
        log.info("Ответ отправлен {}", booking);
        return booking;
    }
}