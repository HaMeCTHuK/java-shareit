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

    private Long id;
    @NotNull
    private LocalDateTime start;
    @NotNull
    private LocalDateTime end;
    private ItemDto item;
    private UserDto booker;
    private BookingStatus status;
}
