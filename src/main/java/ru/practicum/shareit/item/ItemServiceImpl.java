package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

/*    final ItemRepository itemRepository;
    final UserRepository userRepository;




    @Override
    public ItemDto createItem(ItemDto itemDto) {
        User owner = itemRepository.findById(userId);
        Item item = ItemMapper.toItem(itemDto, owner);
        return ItemMapper.toItemDto(itemRepository.save(item));
    }*/
}
