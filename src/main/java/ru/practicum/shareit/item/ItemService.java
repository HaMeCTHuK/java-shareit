package ru.practicum.shareit.item;

import org.springframework.data.crossstore.ChangeSetPersister;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {

    ItemDto createItem(ItemDto itemDto) throws ChangeSetPersister.NotFoundException;

    ItemDto getItem(Long id);

    ItemDto updateItem(Long itemId, ItemDto itemDto);

    //ItemDto updateItem(ItemDto itemDto);

    void deleteItem(Long id);

    List<ItemDto> getAllItems();

    List<ItemDto> getAllItemsWithUserId(Long userId);

    List<ItemDto> searchItemsByText(String text, Long userId);

    Comment addComment(Comment comment) throws ChangeSetPersister.NotFoundException;
}
