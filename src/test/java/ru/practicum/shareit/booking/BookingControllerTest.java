package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoCreate;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.common.FromSizeRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookingControllerTest {

    @Mock
    private BookingServiceImpl bookingService;

    @Mock
    private BookingMapper bookingMapper;

    @InjectMocks
    private BookingController bookingController;

    @Test
    public void testCreateBooking() {
        BookingDtoCreate bookingDtoCreate = new BookingDtoCreate();
        Long userId = 1L;
        BookingDto bookingDto = new BookingDto();

        when(bookingMapper.toBookingFromBookingDtoCreate(bookingDtoCreate, userId)).thenReturn(new Booking());
        when(bookingService.createBooking(any(Booking.class))).thenReturn(bookingDto);

        BookingDto result = bookingController.createBooking(userId, bookingDtoCreate);

        assertNotNull(result);
    }

    @Test
    public void testGetBooking() {
        Long userId = 1L;
        Long bookingId = 1L;
        BookingDto bookingDto = new BookingDto();

        when(bookingService.getBooking(bookingId, userId)).thenReturn(bookingDto);

        BookingDto result = bookingController.getBooking(userId, bookingId);

        assertNotNull(result);
    }

    @Test
    public void testGetOwnerBookings() {
        Long userId = 1L;
        BookingDto bookingDto = new BookingDto();
        bookingDto.setStart(LocalDateTime.now());
        bookingDto.setId(userId);
        List<BookingDto> bookingDtoList = new ArrayList<>();
        bookingDtoList.add(bookingDto);

        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        Pageable pageable = FromSizeRequest.of(0, 5, sort);

        when(bookingService.getOwnerBookingsByState(userId, "ALL", pageable)).thenReturn(bookingDtoList);

        List<BookingDto> result = bookingController.getOwnerBookings(userId, "ALL", 0, 5);

        assertNotNull(result);
    }

    @Test
    public void testGetAllUserBookings() {
        Long userId = 1L;
        BookingDto bookingDto = new BookingDto();
        bookingDto.setStart(LocalDateTime.now());
        bookingDto.setId(userId);
        List<BookingDto> bookingDtoList = new ArrayList<>();
        bookingDtoList.add(bookingDto);

        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        Pageable pageable = FromSizeRequest.of(0, 5, sort); // использование FromSizeRequest для создания Pageable

        when(bookingService.getAllUserBookingsByState(userId, "ALL", pageable)).thenReturn(bookingDtoList);

        List<BookingDto> result = bookingController.getAllUserBookings(userId, "ALL", 0, 10);

        assertNotNull(result);
    }

    @Test
    public void testUpdateBooking() {
        Long userId = 1L;
        Long bookingId = 1L;
        Boolean approved = true;
        BookingDto bookingDto = new BookingDto();

        when(bookingService.updateBooking(userId, bookingId, approved)).thenReturn(bookingDto);

        BookingDto result = bookingController.updateBooking(userId, bookingId, approved);

        assertNotNull(result);
    }

    @Test
    public void testDeleteBooking() {
        Long bookingId = 1L;

        bookingController.deleteBooking(bookingId);

        verify(bookingService, times(1)).deleteBooking(bookingId);
    }
}
