package ru.practicum.shareit.request.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@EqualsAndHashCode(of = {"id"})
@RequiredArgsConstructor
public class ItemRequest {

    private Long id;  //уникальный идентификатор запроса;
    private String description;  //текст запроса, содержащий описание требуемой вещи;
    private User requestor;  //пользователь, создавший запрос;
    private LocalDateTime created;  //дата и время создания запроса.

}
