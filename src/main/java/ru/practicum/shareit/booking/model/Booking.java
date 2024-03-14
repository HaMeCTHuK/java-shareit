package ru.practicum.shareit.booking.model;

import lombok.*;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */

@Getter
@Setter
@Builder
@EqualsAndHashCode(of = {"id"})
@RequiredArgsConstructor
public class Booking {

    private Long id;  //уникальный идентификатор бронирования;
    private LocalDateTime start;  //дата и время начала бронирования;
    private LocalDateTime end;  //дата и время конца бронирования;
    private Item item;  //вещь, которую пользователь бронирует;
    private User booker;  //пользователь, который осуществляет бронирование;
    private BookingStatus status;  //статус бронирования.

    public boolean isFinished(LocalDateTime now) {
        return getStatus() == BookingStatus.APPROVED && getEnd().isBefore(now);
    }

}
