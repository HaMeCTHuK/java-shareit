package ru.practicum.shareit.item.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@SuperBuilder
@EqualsAndHashCode(of = {"id"})
@RequiredArgsConstructor
public class ItemDto {
    private Long id;  //уникальный идентификатор вещи;
    @NotEmpty
    private String name;  //краткое название;
    @NotEmpty
    private String description;  //развёрнутое описание;
    @NotNull
    private Boolean available;  //статус о том, доступна или нет вещь для аренды;
    private UserDto owner;  //владелец вещи;
    private ItemRequest request;  //если вещь была создана по запросу другого пользователя, то в этом
    // поле будет храниться ссылка на соответствующий запрос.
}
