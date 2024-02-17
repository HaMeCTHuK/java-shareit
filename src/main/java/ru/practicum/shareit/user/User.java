package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

/**
 * TODO Sprint add-controllers.
 */

@Data
@SuperBuilder
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private Long id;  //уникальный идентификатор пользователя;
    private String name;  //имя или логин пользователя;
    @Email
    @NotNull
    private String email;  //адрес электронной почты (учтите, что два пользователя не могут
                           //иметь одинаковый адрес электронной почты)

}
