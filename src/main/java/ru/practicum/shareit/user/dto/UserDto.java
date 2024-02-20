package ru.practicum.shareit.user.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
@SuperBuilder
@EqualsAndHashCode(of = {"id"})
@RequiredArgsConstructor
public class UserDto {
    private Long id;
    @NonNull
    private String name;
    @Email
    @NotNull
    private String email;

}
