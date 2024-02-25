package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DataNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ItemMapper itemMapper;
    private final UserMapper userMapper;

    @Override
    public ItemDto createItem(ItemDto itemDto) {
        User owner = userMapper.toUser(itemDto.getOwner());
        if (userRepository.get(owner.getId()) == null) {
            throw new DataNotFoundException("Owner не найден");
        }

        itemDto.setId(itemRepository.getGenerateId());
        Item item = itemMapper.toItem(itemDto);
        item.setOwner(owner);
        item = itemRepository.save(item);

        return itemMapper.toItemDto(item);
    }

    @Override
    public ItemDto updateItem(ItemDto itemDto) {
        Item item = itemMapper.toItem(itemDto);
        User user = userMapper.toUser(itemDto.getOwner());
        Item existingItem = itemRepository.get(item.getId());

        if (item.getName() != null) {
            existingItem.setName(item.getName());
        }
        if (item.getDescription() != null) {
            existingItem.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            existingItem.setAvailable(item.getAvailable());
        }

        existingItem.setOwner(user);

        Item savedItem = itemRepository.save(existingItem);
        ItemDto reciveItemDto = itemMapper.toItemDto(savedItem);

        return reciveItemDto;
    }

    @Override
    public ItemDto getItem(Long id) {
        Item item = itemRepository.get(id);
        if (item == null) {
            throw new DataNotFoundException("Item не найден");
        }
        ItemDto itemDto = itemMapper.toItemDto(item);
        return itemDto;
    }

    @Override
    public void deleteItem(Long id) {
        if (itemRepository.get(id) == null) {
            throw new DataNotFoundException("Item не найден");
        }
        itemRepository.delete(id);
    }

    @Override
    public List<ItemDto> getAllItems() {
        List<Item> allItems = itemRepository.getAllItems();
        List<ItemDto> allItemsDto = allItems.stream()
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
        return allItemsDto;
    }

    @Override
    public List<ItemDto> getAllItemsWithUserId(Long userId) {
        List<Item> allItems = itemRepository.getAllItems();
        List<ItemDto> userItemsDto = allItems.stream()
                .filter(item -> item.getOwner() != null && item.getOwner().getId().equals(userId))
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
        return userItemsDto;
    }

    @Override
    public List<ItemDto> searchItemsByText(String text, Long userId) {
        List<Item> allItems = itemRepository.getAllItems();
        List<ItemDto> foundItemsDto = allItems.stream()
                .filter(item -> item.getAvailable()
                        && item.getDescription().toLowerCase().contains(text.toLowerCase()))
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
        return foundItemsDto;
    }
}
