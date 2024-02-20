package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.DataAlreadyExistException;
import ru.practicum.shareit.exception.DataNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

/**
 * TODO Sprint add-controllers.
 */

@Slf4j
@RestController
@RequestMapping("/items")
public class ItemController {

  /*  @Autowired
    private ItemService itemService;

    @PostMapping
    public ItemDto createItem(@RequestHeader("X-Sharer-User-Id") Long userId, @Validated({Create.class}) @RequestBody ItemDto itemDto) {
        //validateUser(user);
        log.info("Пытаемся добавить item: {}", itemDto);
        return itemService.createItem(itemDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateUser(@PathVariable Long itemId,@RequestBody ItemDto itemDto) {
        user.setId(itemId);

        User existingUser = itemService.getItem(userId);
        if (existingUser == null) {
            throw new DataNotFoundException("Пользователь с ID " + userId + " не найден");
        }

        for (Item addedItem : getAllItems()) {
            if (addedItem.getEmail().equals(user.getEmail()) && !addedItem.getId().equals(userId)) {
                throw new DataAlreadyExistException("Пользователь уже сужествует");
            }
        }

        log.info("Пытаемся обновить пользователя : {}", user);
        return itemService.updateItem(user);
    }*/
}
