package ru.practicum.shareit.request.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@RequiredArgsConstructor
public class ItemResponseOnRequestDto {

    private Long id; //itemId
    private String name; //item name
    private String description;  //item description
    private Long requestId;  // requestId
    private Boolean available;  //item available
}
