package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {

    ItemDto createItem(ItemDto itemDto);

    ItemDto getItem(Long id);

    ItemDto updateItem(ItemDto itemDto);

    void deleteItem(Long id);

    List<ItemDto> getAllItems();

    List<ItemDto> getAllItemsWithUserId(Long userId);

    List<ItemDto> searchItemsByText(String text, Long userId);
}
