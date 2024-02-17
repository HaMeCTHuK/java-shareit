package ru.practicum.shareit.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ru.practicum.shareit.user.User;

import java.time.LocalDate;

/**
 * TODO Sprint add-item-requests.
 */

@Data
@SuperBuilder
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
public class ItemRequest {

    private Long id;  //уникальный идентификатор запроса;
    private String description;  //текст запроса, содержащий описание требуемой вещи;
    private User requestor;  //пользователь, создавший запрос;
    private LocalDate created;  //дата и время создания запроса.

}
