package ru.practicum.shareit.booking.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */

@Data
@SuperBuilder
@EqualsAndHashCode(of = {"id"})
@RequiredArgsConstructor
public class BookingDto {

    private Long id;  //уникальный идентификатор бронирования;
    private LocalDateTime start;  //дата и время начала бронирования;
    private LocalDateTime end;  //дата и время конца бронирования;
    private ItemDto item;  //вещь, которую пользователь бронирует;
    private UserDto booker;  //пользователь, который осуществляет бронирование;
    private BookingStatus status;  //статус бронирования.
}
