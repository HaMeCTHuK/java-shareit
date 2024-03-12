package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.practicum.shareit.booking.entity.BookingEntity;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.DataNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.entity.ItemEntity;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.entity.UserEntity;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final ItemMapper itemMapper;
    private final UserMapper userMapper;
    private Long generatedId = 0L;

    @Override
    public ItemDto createItem(ItemDto itemDto) {
        User owner = userMapper.toUser(itemDto.getOwner());
        if (userRepository.get(owner.getId()) == null) {
            throw new DataNotFoundException("Owner не найден");
        }

        itemDto.setId(++generatedId);
        Item item = itemMapper.toItem(itemDto);
        item.setOwner(owner);
        item = itemRepository.save(item);

        return itemMapper.toItemDto(item);
    }

/*    @Override
    public Item createItem(Item item) throws ChangeSetPersister.NotFoundException {
        item.setOwner(userService.getUser(item.getOwner().getId()));
        try {
            return itemMapper.toItem(itemRepository.save(itemMapper.toEntity(item)));
        } catch (Exception exception) {
            throw new ValidationException("проблемсссссс");
        }
    }*/

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

/*    @Override
    public Item updateItem(Long itemId, Item item) {
        validate(itemId, item);
        try {
            ItemEntity stored = itemRepository.findById(itemId)
                    .orElseThrow(ChangeSetPersister.NotFoundException::new);
            itemMapper.updateEntity(item, stored);
            return itemMapper.toItem(itemRepository.save(stored));
        } catch (ChangeSetPersister.NotFoundException e) {
            throw new ValidationException("Езда не по плану");
        }
    }*/

    @Override
    public ItemDto getItem(Long id) {
        Item item = itemRepository.get(id);
        if (item == null) {
            throw new DataNotFoundException("Item не найден");
        }
        ItemDto itemDto = itemMapper.toItemDto(item);
        return itemDto;
    }

/*    @Override
    public Item getItem(Long itemId, Long userId) {
        return itemRepository.findById(itemId)
                .map(itemMapper::toItem)
                .map(item -> {
                    final ItemEntity itemEntity = itemMapper.toEntity(item);
                    if (Objects.equals(((Item) item).getOwner().getId(), userId) {
                        final Timestamp start = Timestamp.valueOf(LocalDateTime.now());
                        final BookingEntity lastBooking = bookingRepository
                                .findFirstByItemAndStartBeforeOrderByStartDesc(itemEntity, start)
                                .orElse(null);
                        final BookingEntity nextBooking = bookingRepository
                                .findFirstByItemAndStartAfterOrderByStart(itemEntity, start)
                                .orElse(null);
                        item.setLastBooking(mapper.toItemBooking(lastBooking));
                        item.setNextBooking(mapper.toItemBooking(nextBooking));
                    }
                    item.setComments(commentsRepository.FindAllByItem(itemEntity).stream()
                            .map(commentMapper::toItemComment)
                            .collect(Collectors.toList()));
                })
                .orElseThrow(ChangeSetPersister.NotFoundException::new);
    }*/

    @Override
    public void deleteItem(Long id) {
        if (itemRepository.get(id) == null) {
            throw new DataNotFoundException("Item не найден");
        }
        itemRepository.delete(id);
    }

/*    @Override
    public void deleteItem(Long id) {
        ItemEntity itemEntity = itemRepository.getReferenceById(id);
        itemRepository.delete(itemEntity);
    }*/

    @Override
    public List<ItemDto> getAllItems() {
        List<Item> allItems = itemRepository.getAllItems();
        List<ItemDto> allItemsDto = allItems.stream()
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
        return allItemsDto;
    }

/*    @Override
    public List<Item> getAllItems(Long userId) throws ChangeSetPersister.NotFoundException {
        UserEntity userEntity = userMapper.toEntity(userService.getUser(userId));
        return itemRepository.findAllByOwnerOrderById(userEntity).stream()
                .map(itemMapper::toItem)
                .peek(item -> {
                    final ItemEntity itemEntity = itemMapper.toEntity(item);
                    if (Objects.equals(item.getOwner().getId(), userId)) {
                        final Timestamp start = Timestamp.valueOf(LocalDateTime.now());
                        final BookingEntity lastBooking = bookingRepository
                                .findFirstByItemAndStartBeforeOrderByStartDesc(itemEntity, start)
                                .orElse(null);
                        final BookingEntity nextBooking = bookingRepository
                                .findFirstByItemAndStartAfterOrderByStart(itemEntity, start)
                                .orElse(null);
                        item.setLastBooking(mapper.toItemBooking(lastBooking));
                        item.setNextBooking(mapper.toItemBooking(nextBooking));
                    }
                    item.setComments(commentRepository.findAllByItem(itemEntity).stream()
                            .map(mapper:: toTitemComment)
                            .collect(Collectors.toList()));
                })
                .collect(Collectors.toList());
    }*/

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

/*    @Override
    public List<Item> searchItemsByText(String text, Long userId) {
        if (!StringUtils.hasText(text)) {
            return Collections.emptyList();
        }
        return itemRepository.search(text).stream()
                .map(itemMapper::toItem)
                .collect(Collectors.toList());
    }*/

    @Override
    public Comment addComment(Comment comment) throws ChangeSetPersister.NotFoundException {
        ItemEntity itemEntity = itemRepository.findById(comment.getId())
                .orElseThrow(ChangeSetPersister.NotFoundException::new);
    }
}
