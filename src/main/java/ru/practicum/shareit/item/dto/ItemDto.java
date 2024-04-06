package ru.practicum.shareit.item.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
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

    private Long id;
    @NotNull
    @NotEmpty
    private String name;
    @NotNull
    private String description;
    @NotNull
    private Boolean available;
    private UserDto owner;
    private Long requestId;
    private Item.ItemBooking lastBooking;
    private Item.ItemBooking nextBooking;
    private List<Item.ItemComment> comments;


    @Getter
    @Setter
    @AllArgsConstructor
    public static class ItemBooking {

        private Long id;
        private LocalDateTime start;
        private LocalDateTime end;
        private Long itemId;
        private Long bookerId;
        private BookingStatus status;

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
