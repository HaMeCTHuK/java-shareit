package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.entity.BookingEntity;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.DataNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.entity.ItemEntity;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.entity.UserEntity;
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

    @Override
    public BookingDto createBooking(Booking booking) {
        ItemEntity item = itemRepository.findById(booking.getItem().getId())
                .orElseThrow(() -> new DataNotFoundException("Item with id " + booking.getItem().getId() + " not found"));

        validate(booking, item);

        if (!item.getAvailable()) {
            throw new ValidationException("Item with id " + booking.getItem().getId() + " is not available for booking");
        }

        UserEntity owner = userRepository.findById(booking.getBooker().getId())
                .orElseThrow(() -> new DataNotFoundException("User with id " + booking.getBooker().getId() + " not found"));

        BookingEntity entity = bookingMapper.toEntity(booking);
        entity.setItem(item);
        entity.setBooker(owner);
        entity.setStatus(BookingStatus.WAITING);
        BookingEntity savedBooking = bookingRepository.save(entity);

        return bookingMapper.toDto(savedBooking);
    }


    @Transactional
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

    @Override
    public List<BookingDto> findCurrentByOwnerItems(Long userId) {
        UserEntity owner = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("User with id " + userId + " not found"));
        List<BookingEntity> userBookings = bookingRepository.findCurrentByOwnerItems(owner, Timestamp.valueOf(LocalDateTime.now()));
        return bookingMapper.toBookingDtoList(userBookings);
    }

    @Override
    public List<BookingDto> findPastByOwnerItems(Long userId) {
        UserEntity owner = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("User with id " + userId + " not found"));
        List<BookingEntity> userBookings = bookingRepository.findPastByOwnerItems(owner, Timestamp.valueOf(LocalDateTime.now()));
        return bookingMapper.toBookingDtoList(userBookings);
    }

    @Override
    public List<BookingDto> findCurrentByBooker(Long userId) {
        UserEntity owner = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("User with id " + userId + " not found"));
        List<BookingEntity> userBookings = bookingRepository.findCurrentByBooker(owner, Timestamp.valueOf(LocalDateTime.now()));
        return bookingMapper.toBookingDtoList(userBookings);
    }

    @Override
    public List<BookingDto> findPastByBooker(Long userId) {
        UserEntity owner = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("User with id " + userId + " not found"));
        List<BookingEntity> userBookings = bookingRepository.findPastByBooker(owner, Timestamp.valueOf(LocalDateTime.now()));
        return bookingMapper.toBookingDtoList(userBookings);
    }

    public List<BookingDto> getOwnerBookingsByState(Long userId, String state) {
        BookingStatus status;
        switch (state) {
        case "CURRENT":
            return findCurrentByOwnerItems(userId);
        case "PAST":
            return findPastByOwnerItems(userId);
        case "ALL":
            status = BookingStatus.ALL;
            return findAllByOwnerItemsAndStatus(userId, status);
        case "FUTURE":
            status = BookingStatus.FUTURE;
            return findAllByOwnerItemsAndStatus(userId, status);
        case "WAITING":
            status = BookingStatus.WAITING;
            return findAllByOwnerItemsAndStatus(userId, status);
        case "REJECTED":
            status = BookingStatus.REJECTED;
            return findAllByOwnerItemsAndStatus(userId, status);
        case "CANCELED":
            status = BookingStatus.CANCELED;
            return findAllByOwnerItemsAndStatus(userId, status);
        default:
            throw new ValidationException("Unknown state: " + state);
        }
    }

    @Override
    public List<BookingDto> getAllUserBookingsByState(Long userId, String state) {
        BookingStatus status;
        switch (state) {
        case "CURRENT":
            return findCurrentByBooker(userId);
        case "PAST":
            return findPastByBooker(userId);
        case "ALL":
            status = BookingStatus.ALL;
            return getUserBookings(userId, status);
        case "FUTURE":
            status = BookingStatus.FUTURE;
            return getUserBookings(userId, status);
        case "WAITING":
            status = BookingStatus.WAITING;
            return getUserBookings(userId, status);
        case "REJECTED":
            status = BookingStatus.REJECTED;
            return getUserBookings(userId, status);
        case "CANCELED":
            status = BookingStatus.CANCELED;
            return getUserBookings(userId, status);
        default:
            throw new ValidationException("Unknown state: " + state);
        }
    }


    private void validate(Booking booking, ItemEntity item) {
        UserEntity user = userRepository.findById(booking.getBooker().getId())
                .orElseThrow(() -> new DataNotFoundException("User с id " + booking.getBooker().getId() + " не найден"));

        if (booking.getBooker().getId().equals(item.getOwner().getId())) {
            throw new DataNotFoundException("букер е может быть овнером");
        }

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
