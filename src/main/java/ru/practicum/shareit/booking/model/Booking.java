package ru.practicum.shareit.booking.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDate;

/**
 * TODO Sprint add-bookings.
 */

@Getter
@Setter
@Builder
@EqualsAndHashCode(of = {"id"})
public class Booking {

    private Long id;  //уникальный идентификатор бронирования;
    private LocalDate start;  //дата и время начала бронирования;
    private LocalDate end;  //дата и время конца бронирования;
    private Item item;  //вещь, которую пользователь бронирует;
    private Object booker;  //пользователь, который осуществляет бронирование;
    private String status;  //статус бронирования.

}
