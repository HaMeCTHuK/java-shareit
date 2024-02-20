package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DataNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {


    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ItemMapper itemMapper;
    private final UserMapper userMapper;
    private Long generatedId = 0L;

    @Override
    public ItemDto createItem(ItemDto itemDto, UserDto userDto) {
        User owner = userMapper.toUser(userDto);
        if (userRepository.get(owner.getId()) == null) {
            throw new DataNotFoundException("Owner не найден");
        }

        itemDto.setOwner(owner);
        itemDto.setId(++generatedId);
        Item item = itemMapper.toItem(itemDto);
        item = itemRepository.save(item);

        return itemMapper.toItemDto(item);
    }


}
