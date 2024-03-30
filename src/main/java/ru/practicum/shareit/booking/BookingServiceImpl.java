package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.entity.BookingEntity;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.DataNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.entity.ItemEntity;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.mapper.ItemRepositoryMapper;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.entity.UserEntity;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.mapper.UserRepositoryMapper;
import ru.practicum.shareit.user.repository.UserRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ItemMapper itemMapper;
    private final ItemRepositoryMapper itemRepositoryMapper;
    private final UserMapper userMapper;
    private final UserRepositoryMapper userRepositoryMapper;

    @Override
    public BookingDto createBooking(Booking booking) {
        validate(booking);
        ItemEntity item = itemRepository.findById(booking.getItem().getId())
                .orElseThrow(() -> new DataNotFoundException("Item with id " + booking.getItem().getId() + " not found"));

        if (!item.getAvailable()) {
            throw new ValidationException("Item with id " + booking.getItem().getId() + " is not available for booking");
        }

        UserEntity user = userRepository.findById(booking.getBooker().getId())
                .orElseThrow(() -> new DataNotFoundException("User with id " + booking.getBooker().getId() + " not found"));

        Timestamp startTime = booking.getStart().equals("undefined") ? null : Timestamp.valueOf(booking.getStart());
        Timestamp endTime = booking.getEnd().equals("undefined") ? null : Timestamp.valueOf(booking.getEnd());

        BookingEntity bookingEntity = new BookingEntity();
        bookingEntity.setItem(item);
        bookingEntity.setBooker(user);
        bookingEntity.setStatus(BookingStatus.WAITING);
        bookingEntity.setStart(startTime);
        bookingEntity.setEnd(endTime);

        BookingEntity savedBooking = bookingRepository.save(bookingEntity);

        return bookingMapper.toDto(savedBooking);
    }


    @Override
    public BookingDto getBooking(Long bookingId, Long userId) {
        BookingEntity bookingEntity = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new DataNotFoundException("Booking with id " + bookingId + " not found"));
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("User with id " + userId + " not found"));
        if (bookingEntity.getItem().getOwner().getId().equals(userId) ||
                bookingEntity.getBooker().getId().equals(userId)) {
            return bookingMapper.toDto(bookingEntity);
        } else {
            throw new DataNotFoundException("User with id " + userId + " cant get booking (owner or booker)");
        }

    }

    @Override
    public BookingDto updateBooking(Long userId, Long bookingId, Boolean approved) {
        BookingEntity bookingEntity = bookingRepository.findByIdWithItemAndBooker(bookingId)
                .orElseThrow(() -> new DataNotFoundException("Booking with id " + bookingId + " not found"));

        if (!bookingEntity.getItem().getOwner().getId().equals(userId)) {
            throw new DataNotFoundException("Booking with id " + bookingId + " does not belong to user with id " + userId);
        }

        if (bookingEntity.getStatus().equals(BookingStatus.APPROVED)) {
            throw new ValidationException("Статус уже APPROVED");
        }

        bookingEntity.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        BookingEntity updatedBooking = bookingRepository.save(bookingEntity);
        return bookingMapper.toDto(updatedBooking);
    }


    @Override
    public void deleteBooking(Long bookingId) {
        bookingRepository.deleteById(bookingId);
    }

    @Override
    public List<BookingDto> findAllByOwnerItemsAndStatus(Long userId, BookingStatus status) {
        UserEntity owner = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("User with id " + userId + " not found"));

        if (status.equals(BookingStatus.ALL)) {
            List<BookingEntity> recivedList = bookingRepository.findAllByItemOwnerOrderByIdDesc(owner);
            return bookingMapper.toBookingDtoList(recivedList);
        }

        if (status == BookingStatus.FUTURE) {
            List<BookingEntity> recivedList = bookingRepository.findFutureByOwnerItems(owner, Timestamp.valueOf(LocalDateTime.now()));
            return bookingMapper.toBookingDtoList(recivedList);
        }

        List<BookingEntity> recivedList = bookingRepository.findAllByOwnerItemsAndStatus(owner, status);

        return bookingMapper.toBookingDtoList(recivedList);
    }

    @Override
    public List<BookingDto> getUserBookings(Long userId, BookingStatus status) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("User with id " + userId + " not found"));

        if (status.equals(BookingStatus.ALL)) {
            List<BookingEntity> userBookings =  bookingRepository.findAllByBookerIdOrderByIdDesc(userId);
            return bookingMapper.toBookingDtoList(userBookings);
        }

        if (status == BookingStatus.FUTURE) {
            List<BookingEntity> userBookings = bookingRepository.findFutureByBooker(user, Timestamp.valueOf(LocalDateTime.now()));
            return bookingMapper.toBookingDtoList(userBookings);
        }

        List<BookingEntity> userBookings = bookingRepository.findAllByBookerIdAndStatusSortedByIdDesc(userId, status);

        return bookingMapper.toBookingDtoList(userBookings);
    }

    @Override
    public List<BookingDto> findAllByOwner(Long userId) {
        UserEntity owner = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("User with id " + userId + " not found"));
        List<BookingEntity> bookingList = bookingRepository.findAllByOwnerItemsAndStatus(owner, BookingStatus.ALL);

        return bookingMapper.toBookingDtoList(bookingList);
    }


    private void validate(Booking booking) {
        UserEntity user = userRepository.findById(booking.getBooker().getId())
                .orElseThrow(() -> new DataNotFoundException("User с id " + booking.getBooker().getId() + " не найден"));

        ItemEntity item = itemRepository.findById(booking.getItem().getId())
                .orElseThrow(() -> new DataNotFoundException("Item с id " + booking.getItem().getId() + " не найден"));

        if (booking.getEnd().isBefore(LocalDateTime.now())) {
            throw new ValidationException("End - дата окончания не может быть в прошлом");
        }
        if (booking.getEnd().isBefore(booking.getStart())) {
            throw new ValidationException("End date cannot be before start date");
        }
        if (booking.getStart().isEqual(booking.getEnd())) {
            throw new ValidationException("Start date cannot be equal to end date");
        }
        if (booking.getStart() == null) {
            throw new ValidationException("Start date cannot be null");
        }
        if (booking.getEnd() == null) {
            throw new ValidationException("End date cannot be null");
        }
        if (booking.getStart().isBefore(LocalDateTime.now())) {
            throw new ValidationException("Start date cannot be in the past");
        }
    }
}
