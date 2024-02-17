package ru.practicum.shareit.booking;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDate;

/**
 * TODO Sprint add-bookings.
 */

@Data
@SuperBuilder
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
public class Booking {

    private Long id;  //уникальный идентификатор бронирования;
    private LocalDate start;  //дата и время начала бронирования;
    private LocalDate end;  //дата и время конца бронирования;
    private Item item;  //вещь, которую пользователь бронирует;
    private User booker;  //пользователь, который осуществляет бронирование;
    private String status;  //статус бронирования.

}
