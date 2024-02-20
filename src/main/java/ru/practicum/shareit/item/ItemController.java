package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;

/**
 * TODO Sprint add-controllers.
 */

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    @Autowired
    private final ItemService itemService;
    private final UserService userService;

    @PostMapping
    public ItemDto createItem(@RequestHeader("X-Sharer-User-Id") Long userId, @Valid @RequestBody ItemDto itemDto) {
        UserDto userDto = userService.getUser(userId);
        log.info("Пытаемся добавить item: {}", itemDto);
        return itemService.createItem(itemDto, userDto);
    }

/*    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@PathVariable Long itemId,@RequestBody ItemDto itemDto) {

        Item existingItem = itemService.getItem(itemId);
        if (itemDto == null) {
            throw new DataNotFoundException("Item с ID " + itemId + " не найдена");
        }

        for (Item addedItem : getAllItems()) {
            if (addedItem.getOwner().equals(itemDto.getOwner()) && !addedItem.getId().equals(itemId)) {
                throw new DataAlreadyExistException("Item уже сужествует");
            }
        }

        itemDto.setId(itemId);

        log.info("Пытаемся обновить Item : {}", itemDto);
        return itemService.updateItem(itemDto);
    }*/
}
