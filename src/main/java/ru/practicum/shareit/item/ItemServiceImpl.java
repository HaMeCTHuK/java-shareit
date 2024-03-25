package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.entity.BookingEntity;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.DataNotFoundException;
import ru.practicum.shareit.exception.StorageException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.entity.CommentEntity;
import ru.practicum.shareit.item.entity.ItemEntity;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.mapper.ItemRepositoryMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.entity.UserEntity;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.mapper.UserRepositoryMapper;
import ru.practicum.shareit.user.repository.UserRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final ItemRepositoryMapper itemRepositoryMapper;
    private final UserRepository userRepository;
    private final UserRepositoryMapper userRepositoryMapper;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final BookingMapper bookingMapper;
    @Autowired
    private final UserService userService;
    private final ItemMapper itemMapper;
    private final UserMapper userMapper;

    @Override
    public ItemDto createItem(Item item) {
        UserDto userDto = userService.getUser(item.getOwner().getId());
        item.setOwner(userMapper.toUser(userDto));
        try {
            ItemEntity savedItem = itemRepository.save(itemRepositoryMapper.toEntity(item));
            return itemMapper.toItemDto(itemRepositoryMapper.toItem(savedItem));
        } catch (Exception exception) {
            throw new StorageException("Ошибка создания Item");
        }
    }

    @Override
    public ItemDto updateItem(Long itemId, ItemDto itemDto) {
        //validate(itemId, item);
        try {
            ItemEntity stored = itemRepository.findById(itemId)
                    .orElseThrow(DataNotFoundException::new);
            Item item = itemMapper.toItemDtoFromUpdateRequest(itemDto,itemDto.getOwner().getId());
            itemRepositoryMapper.updateEntity(item, stored);
            Item savedItem  = itemRepositoryMapper.toItem(itemRepository.save(stored));
            return itemMapper.toItemDto(savedItem);
        } catch (StorageException e) {
            throw new StorageException("Ошибка обновления Item");
        }
    }

    @Override
    public ItemDto getItem(Long itemId) {
        if (!itemRepository.existsById(itemId)) {
            throw new DataNotFoundException("Item не найден");
        }
        ItemEntity itemEntity = itemRepository.getReferenceById(itemId);
        ItemDto itemDto = itemMapper.toItemDto(itemRepositoryMapper.toItem(itemEntity));
        return itemDto;
    }

    @Override
    public Item getItemWithUserId(Long itemId, Long userId) {
        if (!itemRepository.existsById(itemId)) {
            throw new DataNotFoundException("Item не найден");
        }
        return itemRepository.findById(itemId)
                .map(itemRepositoryMapper::toItem)
                .map(item -> {
                    final ItemEntity itemEntity = itemRepositoryMapper.toEntity(item);
                    if (Objects.equals(item.getOwner().getId(), userId)) {
                        final Timestamp start = Timestamp.valueOf(LocalDateTime.now());
                        final BookingEntity lastBooking = bookingRepository
                                .findFirstByItemAndStartBeforeOrderByStartDesc(itemEntity, start)
                                .orElse(null);
                        final BookingEntity nextBooking = bookingRepository
                                .findFirstByItemAndStartAfterOrderByStart(itemEntity, start)
                                .orElse(null);
                        item.setLastBooking(itemRepositoryMapper.toItemBooking(lastBooking));
                        item.setNextBooking(itemRepositoryMapper.toItemBooking(nextBooking));
                    }
                    item.setComments(commentRepository.findAllByItem(itemEntity).stream()
                            .map(commentMapper::toItemComment)
                            .collect(Collectors.toList()));
                    return item;
                })
                .orElseThrow(StorageException::new);
    }

    @Override
    public void deleteItem(Long id) {
        ItemEntity itemEntity = itemRepository.getReferenceById(id);
        itemRepository.delete(itemEntity);
    }

    @Override
    public List<ItemDto> getAllItems() {
        List<ItemEntity> allItems = itemRepository.findAll();
        List<ItemDto> allItemsDto = allItems.stream()
                .map(itemRepositoryMapper::toItem)
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
        return allItemsDto;
    }

    @Override
    public List<Item> getAllItemsWithUserId(Long userId) throws DataNotFoundException {
        if (!userRepository.existsById(userId)) {
            throw new DataNotFoundException("User не найден");
        }
        UserDto userDto = userService.getUser(userId);
        UserEntity userEntity = userRepositoryMapper.toEntity(userMapper.toUser(userDto));
        return itemRepository.findAllByOwnerOrderById(userEntity).stream()
                .map(itemRepositoryMapper::toItem)
                .peek(item -> {
                    final ItemEntity itemEntity = itemRepositoryMapper.toEntity(item);
                    if (Objects.equals(item.getOwner().getId(), userId)) {
                        final Timestamp start = Timestamp.valueOf(LocalDateTime.now());
                        final BookingEntity lastBooking = bookingRepository
                                .findFirstByItemAndStartBeforeOrderByStartDesc(itemEntity, start)
                                .orElse(null);
                        final BookingEntity nextBooking = bookingRepository
                                .findFirstByItemAndStartAfterOrderByStart(itemEntity, start)
                                .orElse(null);
                        item.setLastBooking(itemRepositoryMapper.toItemBooking(lastBooking));
                        item.setNextBooking(itemRepositoryMapper.toItemBooking(nextBooking));
                    }
                    item.setComments(commentRepository.findAllByItem(itemEntity).stream()
                            .map(commentMapper:: toItemComment)
                            .collect(Collectors.toList()));
                })
                .collect(Collectors.toList());
    }

/*    @Override
    public List<ItemDto> getAllItemsWithUserId(Long userId) {
        List<ItemEntity> allItems = itemRepository.findAll();
        List<ItemDto> userItemsDto = allItems.stream()
                .filter(item -> item.getOwner() != null && item.getOwner().getId().equals(userId))
                .map(itemRepositoryMapper::toItem)
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
        return userItemsDto;
    }*/

    @Override
    public List<ItemDto> searchItemsByText(String text, Long userId) {
        List<ItemEntity> allItems = itemRepository.findAll();
        List<ItemDto> foundItemsDto = allItems.stream()
                .filter(item -> item.getAvailable()
                        && item.getDescription().toLowerCase().contains(text.toLowerCase()))
                .map(itemRepositoryMapper::toItem)
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
        return foundItemsDto;
    }

    @Override
    public List<ItemDto> getUsersItems(String email) {
        return null;
    }

      @Override
      public CommentDto addComment(Comment comment) throws DataNotFoundException {
          ItemEntity itemEntity = itemRepository.findById(comment.getId())
                  .orElseThrow(DataNotFoundException::new);
          UserEntity booker = userRepositoryMapper.toEntity(userMapper.toUser(userService.getUser(comment.getAuthorId())));

          LocalDateTime now = LocalDateTime.now();
          List<Booking> bookings = bookingRepository.findAllByItemAndBooker(itemEntity, booker).stream()
                  .map(bookingMapper::toBooking)
                  .collect(Collectors.toList());
          if (bookings.stream().noneMatch(b -> b.isFinished(now))) {
              throw new ValidationException("ошибка валидации");
          }

          comment.setCreated(now);
          try {
              CommentEntity commentEntity = commentMapper.toCommentEntity(comment);
              commentEntity.setItem(itemEntity);
              commentEntity.setAuthor(booker);
              return commentMapper.toCommentDto(commentRepository.save(commentEntity));
          } catch (Exception e) {
              throw new ValidationException("error");
          }
      }
    private void validate(Long itemId, ItemDto itemDto) {
    }
}
