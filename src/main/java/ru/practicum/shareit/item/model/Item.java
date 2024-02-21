package ru.practicum.shareit.item.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * TODO Sprint add-controllers.
 */

@Data
@SuperBuilder
@EqualsAndHashCode(of = {"id"})
@RequiredArgsConstructor
public class Item {
    private Long id;  //уникальный идентификатор вещи;
    @NotEmpty
    private String name;  //краткое название;
    @NotEmpty
    private String description;  //развёрнутое описание;
    private boolean available;  //статус о том, доступна или нет вещь для аренды;
    private User owner;  //владелец вещи;
    private ItemRequest request;  //если вещь была создана по запросу другого пользователя, то в этом
                             // поле будет храниться ссылка на соответствующий запрос.
}
