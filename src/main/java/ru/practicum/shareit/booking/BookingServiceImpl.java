package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.entity.BookingEntity;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.DataNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;

    @Override
    public BookingDto createBooking(Booking booking) {
        BookingEntity savedBooking = bookingRepository.save(bookingMapper.toEntity(booking));
        return bookingMapper.toDto(savedBooking);
    }

    @Override
    public BookingDto getBooking(Long bookingId) {
        BookingEntity bookingEntity = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new DataNotFoundException("Booking with id " + bookingId + " not found"));
        return bookingMapper.toDto(bookingEntity);
    }

    @Override
    public List<BookingDto> getUserBookings(Long userId) {
        List<BookingEntity> userBookings = bookingRepository.findAllByBookerId(userId);
        return userBookings.stream()
                .map(bookingMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public BookingDto updateBooking(Long userId, Long bookingId, BookingDto bookingDto) {
        BookingEntity bookingEntity = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new DataNotFoundException("Booking with id " + bookingId + " not found"));

        if (!bookingEntity.getBooker().getId().equals(userId)) {
            throw new DataNotFoundException("Booking with id " + bookingId + " does not belong to user with id " + userId);
        }

        // Обновляем данные бронирования
        // Примерно так:
        // bookingEntity.setStatus(bookingDto.getStatus());
        // bookingEntity.setStart(bookingDto.getStart());
        // bookingEntity.setEnd(bookingDto.getEnd());

        BookingEntity updatedBooking = bookingRepository.save(bookingEntity);
        return bookingMapper.toDto(updatedBooking);
    }

    @Override
    public void deleteBooking(Long bookingId) {
        bookingRepository.deleteById(bookingId);
    }
}
