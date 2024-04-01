package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {

    ItemDto createItem(Item item);

    ItemDto getItem(Long id);

    Item getItemWithUserId(Long id, Long userId);

    ItemDto updateItem(Long itemId, ItemDto itemDto);

    void deleteItem(Long id);

    List<ItemDto> getAllItems();

    List<Item> getAllItemsWithUserId(Long userId);

    List<ItemDto> searchItemsByText(String text, Long userId);

    CommentDto addComment(Comment comment);

}
