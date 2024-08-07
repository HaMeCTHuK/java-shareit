package ru.practicum.shareit.user.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(of = {"id"})
@RequiredArgsConstructor
public class User {

    private Long id;  //уникальный идентификатор пользователя;

    private String name;  //имя или логин пользователя;

    private String email;  //адрес электронной почты (учтите, что два пользователя не могут
                           //иметь одинаковый адрес электронной почты)
}
