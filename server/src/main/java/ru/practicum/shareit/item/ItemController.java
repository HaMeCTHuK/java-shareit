package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.DataAlreadyExistException;
import ru.practicum.shareit.exception.DataNotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.ArrayList;
import java.util.List;

import static ru.practicum.shareit.common.Constants.X_SHARER_USER_ID;

@Slf4j
@RestController
@RequestMapping(path = "/items")
@RequiredArgsConstructor
public class ItemController {

    @Autowired
    private final ItemService itemService;
    @Autowired
    private final UserService userService;
    private final ItemMapper itemMapper;
    private final CommentMapper commentMapper;

    @PostMapping
    public ItemDto createItem(@RequestHeader(X_SHARER_USER_ID) Long userId, @RequestBody ItemDto itemDto) {
        Item item = itemMapper.toItemFromItemDtoCreate(itemDto, userId);
        log.info("Пытаемся добавить item: {}", item);
        return itemService.createItem(item);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader(X_SHARER_USER_ID) Long userId,
                              @PathVariable Long itemId, @RequestBody ItemDto itemDto) {
         UserDto userDto = userService.getUser(userId);
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

    @GetMapping("/{itemId}")
    public ItemDto getItemWithUserId(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                     @PathVariable Long itemId) {
        log.info("Получаем объект по id: {}", itemId);
        Item item = itemService.getItemWithUserId(itemId, userId);
        return itemMapper.toItemDto(item);
    }


    @DeleteMapping("/{itemId}")
    public void deleteItemById(@PathVariable Long itemId) {
        if (itemService.getItem(itemId) == null) {
            throw new DataNotFoundException("Пользователь с ID " + itemId + " не найден");
        }
        log.info("Удаляем пользователя по ID: " + itemId);
        itemService.deleteItem(itemId);
    }

    @ResponseBody
    @GetMapping
    public List<ItemDto> getAllItemsWithUserId(@RequestHeader(X_SHARER_USER_ID) Long userId) {
        List<Item> allItemsDtoWithUserId = itemService.getAllItemsWithUserId(userId);
        List<ItemDto> allItemsWithUserId = itemMapper.toItemsDtoList(allItemsDtoWithUserId);
        log.info("Получаем items с пользователем ID: {}", userId);
        return allItemsWithUserId;
    }

    @GetMapping("/search")
    public List<ItemDto> searchItemsByText(@RequestHeader(X_SHARER_USER_ID) Long userId, @RequestParam String text) {
        log.info("Вызван метод searchItemsByQuery - поиск items с text(description) " + text + " c userId " + userId);
        if (text.isEmpty()) {
            return new ArrayList<>();
        }
        return itemService.searchItemsByText(text, userId);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(@PathVariable Long itemId,
                             @RequestHeader(X_SHARER_USER_ID) Long userId, @RequestBody CommentDto commentDto) {
        log.info("Вызван метод createComment - поиск items с id " + itemId + " c userId " + userId);
        return itemService.addComment(commentMapper.toCommentWithIds(commentDto, itemId, userId));
    }
}
