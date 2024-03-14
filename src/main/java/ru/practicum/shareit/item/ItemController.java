package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.DataAlreadyExistException;
import ru.practicum.shareit.exception.DataNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;

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
    public ItemDto createItem(@RequestHeader("X-Sharer-User-Id") Long userId, @Valid @RequestBody ItemDto itemDto) throws ChangeSetPersister.NotFoundException {
        if (itemDto.getAvailable() == null || !itemDto.getAvailable()) {
            throw new ValidationException("Available == false || null");
        }
        UserDto userDto = userService.getUser(userId);
        itemDto.setOwner(userDto);
        log.info("Пытаемся добавить item: {}", itemDto);
        return itemService.createItem(itemDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long itemId, @RequestBody ItemDto itemDto) throws ChangeSetPersister.NotFoundException {
         UserDto userDto = userService.getUser(userId);
         ItemDto recivedItemDto = itemService.getItem(itemId);

        if (userId == null || userDto == null) {
            throw new DataNotFoundException("Item с ID владельца " + userId + " не найден");
        }

        if (!recivedItemDto.getOwner().getId().equals(userId)) {
            throw new DataNotFoundException("Только владелец может менять данные item");
        }

        if (itemDto == null || itemId == null) {
            throw new DataNotFoundException("Item не найден");
        }

        itemDto.setId(itemId);
        itemDto.setOwner(userDto);
        for (ItemDto addedItem : itemService.getAllItems()) {
            if (addedItem.getOwner().getEmail().equals(userDto.getEmail()) && !addedItem.getOwner().getId().equals(userDto.getId())) {
                throw new DataAlreadyExistException("Item уже существует");
            }
        }

        log.info("Пытаемся обновить Item : {}", itemDto);
        return itemService.updateItem(itemId, itemDto);
    }

    @GetMapping("/{id}")
    public ItemDto getItem(@RequestBody @PathVariable Long id) {
        log.info("Получаем объект по id: {}", id);
        return itemService.getItem(id);
    }

    @DeleteMapping("/{itemId}")
    public void deleteItemById(@PathVariable Long itemId) {
        if (itemService.getItem(itemId) == null) {
            throw new DataNotFoundException("Пользователь с ID " + itemId + " не найден");
        }
        log.info("Удаляем пользователя по ID: " + itemId);
        itemService.deleteItem(itemId);
    }

//Временно убрал
/*    @GetMapping
    @ResponseBody
    public List<ItemDto> getAllItems() {
        List<ItemDto> allItems = itemService.getAllItems();
        log.info("Текущее количество пользователей: {}", allItems.size());
        return allItems;
    }*/

    @GetMapping
    @ResponseBody
    public List<ItemDto> getAllItemsWithUserId(@RequestHeader("X-Sharer-User-Id") Long userId) {
        List<ItemDto> allItemsWithUserId = itemService.getAllItemsWithUserId(userId);
        log.info("Получаем items с пользователем ID: {}", userId);
        return allItemsWithUserId;
    }

    @GetMapping("/search")
    public List<ItemDto> searchItemsByText(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestParam String text) {
        log.info("Вызван метод searchItemsByQuery - поиск items с text(description) " + text + " c userId " + userId);
        if (text.isEmpty()) {
            return new ArrayList<>();
        }
        return itemService.searchItemsByText(text, userId);
    }

/*    @PostMapping("/{itemId}/comment")
    public CommentDto create(@Min(1L) @PathVariable Long itemId,
                             @RequestHeader("X-Sharer-User-Id") Long userId, CommentCreateRequst request) {
        return null
    }*/
}
