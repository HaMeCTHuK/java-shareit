package ru.practicum.shareit.booking.mapper;

import org.mapstruct.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoCreate;
import ru.practicum.shareit.booking.entity.BookingEntity;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.user.mapper.UserMapper;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, uses = {UserMapper.class,
        ItemMapper.class})
public interface BookingMapper {

    @Mapping(target = "start", source = "start", qualifiedByName = "toLocalDateTime")
    @Mapping(target = "end", source = "end", qualifiedByName = "toLocalDateTime")
    Booking toBooking(BookingEntity bookingEntity);

    @Mapping(target = "booker.id", source = "userId")
    @Mapping(target = "item.id", source = "bookingDtoCreate.itemId")
    Booking toBookingFromBookingDtoCreate(BookingDtoCreate bookingDtoCreate, Long userId);

    @Mapping(target = "start", source = "start", qualifiedByName = "toTimeStamp")
    @Mapping(target = "end", source = "end", qualifiedByName = "toTimeStamp")
    @Mapping(target = "item.request.created", source = "item.request.created", qualifiedByName = "toTimeStamp")
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

    @Mapping(target = "start", source = "start", qualifiedByName = "toLocalDateTime")
    @Mapping(target = "end", source = "end", qualifiedByName = "toLocalDateTime")
    List<BookingDto> toBookingDtoList(List<BookingEntity> recivedList);
}
