package ru.practicum.shareit.item.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ru.practicum.shareit.request.ItemRequest;

/**
 * TODO Sprint add-controllers.
 */

@Data
@SuperBuilder
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
public class Item {
    private Long id;  //уникальный идентификатор вещи;
    private String name;  //краткое название;
    private String description;  //развёрнутое описание;
    private boolean available;  //статус о том, доступна или нет вещь для аренды;
    private String owner;  //владелец вещи;
    private ItemRequest request;  //если вещь была создана по запросу другого пользователя, то в этом
                             // поле будет храниться ссылка на соответствующий запрос.
}
