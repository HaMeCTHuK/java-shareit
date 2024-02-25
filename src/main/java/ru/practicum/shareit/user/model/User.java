package ru.practicum.shareit.user.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

/**
 * TODO Sprint add-controllers.
 */

@Data
@SuperBuilder
@EqualsAndHashCode(of = {"id"})
@RequiredArgsConstructor
public class User {

    private Long id;  //уникальный идентификатор пользователя;
    @NotNull
    private String name;  //имя или логин пользователя;
    @Email
    @NotNull
    private String email;  //адрес электронной почты (учтите, что два пользователя не могут
                           //иметь одинаковый адрес электронной почты)
}
