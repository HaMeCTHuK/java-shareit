package ru.practicum.shareit.request.dto;

import ru.practicum.shareit.user.model.User;

import java.time.LocalDate;

/**
 * TODO Sprint add-item-requests.
 */
public class ItemRequestDto {

    private Long id;  //уникальный идентификатор запроса;
    private String description;  //текст запроса, содержащий описание требуемой вещи;
    private User requestor;  //пользователь, создавший запрос;
    private LocalDate created;  //дата и время создания запроса.

}
