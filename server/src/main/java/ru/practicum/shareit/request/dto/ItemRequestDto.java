package ru.practicum.shareit.request.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

@Data
@SuperBuilder
@EqualsAndHashCode(of = {"id"})
@RequiredArgsConstructor
public class ItemRequestDto {

    private Long id;  //уникальный идентификатор запроса;
    private String description;  //текст запроса, содержащий описание требуемой вещи;
    private UserDto requestor;  //пользователь, создавший запрос;
    private LocalDateTime created;  //дата и время создания запроса.
    private List<ItemResponseOnRequestDto> items;

}
