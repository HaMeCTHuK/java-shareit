package ru.practicum.shareit.booking.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.constraints.NotNull;
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
    @NotNull
    private LocalDateTime start;  //дата и время начала бронирования;
    @NotNull
    private LocalDateTime end;  //дата и время конца бронирования;
    private ItemDto item;  //вещь, которую пользователь бронирует;
    private UserDto booker;  //пользователь, который осуществляет бронирование;
    private BookingStatus status;  //статус бронирования.
}
