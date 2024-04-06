package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingStatus;
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
import ru.practicum.shareit.request.entity.ItemRequestEntity;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.entity.UserEntity;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.mapper.UserRepositoryMapper;
import ru.practicum.shareit.user.repository.UserRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
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
    private final ItemRequestRepository itemRequestRepository;

    @Override
    public ItemDto createItem(Item item) {
        UserDto userDto = userService.getUser(item.getOwner().getId());
        item.setOwner(userMapper.toUser(userDto));
        ItemEntity itemEntity = itemRepositoryMapper.toEntity(item);
        if (item.getRequest().getId() != null) {
            ItemRequestEntity itemRequestEntity = itemRequestRepository.getReferenceById(item.getRequest().getId());
            itemEntity.setRequest(itemRequestEntity);
        }
        try {
            ItemEntity savedItem = itemRepository.save(itemEntity);
            return itemMapper.toItemDto(itemRepositoryMapper.toItem(savedItem));
        } catch (Exception exception) {
            throw new StorageException("Ошибка создания Item");
        }
    }

    @Override
    public ItemDto updateItem(Long itemId, ItemDto itemDto) {
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
        List<ItemEntity> itemEntities = itemRepository.findAllByOwnerOrderById(userEntity);

        Map<Long, List<BookingEntity>> bookingsMap = new HashMap<>();
        List<BookingEntity> bookings = bookingRepository.findAllByItemIn(itemEntities);
        for (BookingEntity booking : bookings) {
            bookingsMap.computeIfAbsent(booking.getItem().getId(), k -> new ArrayList<>()).add(booking);
        }

        return itemEntities.stream()
                .map(itemRepositoryMapper::toItem)
                .peek(item -> {
                    final List<BookingEntity> itemBookings = bookingsMap.getOrDefault(item.getId(), Collections.emptyList());
                    final Timestamp start = Timestamp.valueOf(LocalDateTime.now());
                    final BookingEntity lastBooking = itemBookings.stream()
                            .filter(booking -> booking.getStart().before(start))
                            .max(Comparator.comparing(BookingEntity::getStart))
                            .orElse(null);
                    final BookingEntity nextBooking = itemBookings.stream()
                            .filter(booking -> booking.getStart().after(start))
                            .min(Comparator.comparing(BookingEntity::getStart))
                            .orElse(null);

                    item.setLastBooking(itemRepositoryMapper.toItemBooking(lastBooking));
                    if (nextBooking != null && nextBooking.getStatus() == BookingStatus.REJECTED) {
                        item.setNextBooking(null);
                    } else {
                        item.setNextBooking(itemRepositoryMapper.toItemBooking(nextBooking));
                    }

                    final List<CommentEntity> comments = commentRepository.findAllByItem(itemRepositoryMapper.toEntity(item));
                    item.setComments(comments.stream()
                            .map(commentMapper::toItemComment)
                            .collect(Collectors.toList()));
                })
                .collect(Collectors.toList());
    }

    @Override
    public Item getItemWithUserId(Long itemId, Long userId) {
        if (!itemRepository.existsById(itemId)) {
            throw new DataNotFoundException("Item не найден");
        }

        Map<Long, BookingEntity> bookingsMap = new HashMap<>();
        List<BookingEntity> bookings = bookingRepository.findAllByItemId(itemId);
        bookings.forEach(booking -> bookingsMap.put(booking.getId(), booking));

        ItemEntity itemEntity = itemRepository.findById(itemId)
                .orElseThrow(() -> new DataNotFoundException("Item не найден"));

        Item item = itemRepositoryMapper.toItem(itemEntity);

        if (Objects.equals(item.getOwner().getId(), userId)) {
            final Timestamp start = Timestamp.valueOf(LocalDateTime.now());
            final BookingEntity lastBooking = bookingsMap.values().stream()
                    .filter(booking -> booking.getStart().before(start))
                    .max(Comparator.comparing(BookingEntity::getStart))
                    .orElse(null);
            final BookingEntity nextBooking = bookingsMap.values().stream()
                    .filter(booking -> booking.getStart().after(start))
                    .min(Comparator.comparing(BookingEntity::getStart))
                    .orElse(null);

            item.setLastBooking(itemRepositoryMapper.toItemBooking(lastBooking));
            if (nextBooking != null && nextBooking.getStatus() == BookingStatus.REJECTED) {
                item.setNextBooking(null);
            } else {
                item.setNextBooking(itemRepositoryMapper.toItemBooking(nextBooking));
            }
        }

        List<CommentEntity> comments = commentRepository.findAllByItemId(itemId);
        item.setComments(comments.stream()
                .map(commentMapper::toItemComment)
                .collect(Collectors.toList()));

        return item;
    }

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
      public CommentDto addComment(Comment comment) throws DataNotFoundException {
          ItemEntity itemEntity = itemRepository.findById(comment.getItemId())
                  .orElseThrow(DataNotFoundException::new);

          UserEntity booker = userRepository.findById(comment.getAuthorId())
                  .orElseThrow(DataNotFoundException::new);

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
              CommentEntity savedCommentEntity = commentRepository.save(commentEntity);
              return commentMapper.toCommentDto(savedCommentEntity);
          } catch (Exception e) {
              throw new ValidationException("error");
          }
      }
}
