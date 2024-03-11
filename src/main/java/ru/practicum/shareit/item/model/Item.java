package ru.practicum.shareit.item.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */

@Data
@SuperBuilder
@EqualsAndHashCode(of = {"id"})
@RequiredArgsConstructor
public class Item {

    private Long id;  //уникальный идентификатор вещи;
    private String name;  //краткое название;
    private String description;  //развёрнутое описание;
    private Boolean available;  //статус о том, доступна или нет вещь для аренды;
    private User owner;  //владелец вещи;
    private ItemRequest request;  //если вещь была создана по запросу другого пользователя, то в этом
    // поле будет храниться ссылка на соответствующий запрос.
    private ItemBooking lastBooking;
    private ItemBooking nextBooking;
    private List<ItemComment> comments;


    @Getter
    @Setter
    @AllArgsConstructor
    public static class ItemBooking {

        private Long id;  //уникальный идентификатор бронирования;
        private LocalDate start;  //дата и время начала бронирования;
        private LocalDate end;  //дата и время конца бронирования;
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




