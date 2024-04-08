package ru.practicum.shareit.request.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

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
