package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

public interface ItemService {

    ItemDto createItem(ItemDto itemDto, UserDto userDto);
}
