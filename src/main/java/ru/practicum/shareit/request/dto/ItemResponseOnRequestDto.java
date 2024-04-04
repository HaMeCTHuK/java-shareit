package ru.practicum.shareit.request.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(of = {"id"})
@RequiredArgsConstructor
public class ItemResponseOnRequestDto {

    private Long id; //item id
    private String name;
    private String description;
    private Long requestId;
    private Boolean available;
}
