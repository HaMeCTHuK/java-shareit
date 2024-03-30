package ru.practicum.shareit.item.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
@SuperBuilder
@EqualsAndHashCode(of = {"id"})
@RequiredArgsConstructor
public class ItemDto {

    private Long id;  //уникальный идентификатор вещи;
    @NotNull
    @NotEmpty
    private String name;  //краткое название;
    @NotNull
    private String description;  //развёрнутое описание;
    @NotNull
    private Boolean available;  //статус о том, доступна или нет вещь для аренды;
    private UserDto owner;  //владелец вещи;
    //* private ItemRequest request;  //если вещь была создана по запросу другого пользователя, то в этом
     // поле будет храниться ссылка на соответствующий запрос.*/  //Для следующего спринта оставил
    private Item.ItemBooking lastBooking;
    private Item.ItemBooking nextBooking;
    private List<Item.ItemComment> comments;


    @Getter
    @Setter
    @AllArgsConstructor
    public static class ItemBooking {

        private Long id;  //уникальный идентификатор бронирования;
        private LocalDateTime start;  //дата и время начала бронирования;
        private LocalDateTime end;  //дата и время конца бронирования;
        private Long itemId;  //вещь, которую пользователь бронирует;
        private Long bookerId;  //пользователь, который осуществляет бронирование;
        private BookingStatus status;  //статус бронирования.

    }

    @Getter
    @AllArgsConstructor
    public static class ItemComment {

        private final Long id;
        private final String text;
        private final Long itemId;
        private final Long authorId;
        private final String authorName;
        private final LocalDateTime created;
    }
}
