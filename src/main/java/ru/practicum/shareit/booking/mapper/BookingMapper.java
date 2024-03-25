package ru.practicum.shareit.booking.mapper;

import org.mapstruct.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.entity.BookingEntity;
import ru.practicum.shareit.booking.model.Booking;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface BookingMapper {

    @Mapping(target = "start", source = "start", qualifiedByName = "toLocalDateTime")
    @Mapping(target = "end", source = "end", qualifiedByName = "toLocalDateTime")
    Booking toBooking(BookingEntity bookingEntity);

    Booking toBookingFromBookingDtoCreate(BookingDto bookingDto, Long userId);

    @Mapping(target = "start", source = "start", qualifiedByName = "toTimeStamp")
    @Mapping(target = "end", source = "end", qualifiedByName = "toTimeStamp")
    BookingEntity toEntity(Booking booking);

    @Mapping(target = "start", source = "start", qualifiedByName = "toLocalDateTime")
    @Mapping(target = "end", source = "end", qualifiedByName = "toLocalDateTime")
    BookingDto toDto(BookingEntity savedBooking);

    @Named("toLocalDateTime")
    default LocalDateTime toLocalDateTime(Timestamp timestamp) {
        return timestamp.toLocalDateTime();
    }

    @Named("toTimeStamp")
    default Timestamp toTimeStamp(LocalDateTime localDateTime) {
        return Timestamp.valueOf(localDateTime);
    }
}
