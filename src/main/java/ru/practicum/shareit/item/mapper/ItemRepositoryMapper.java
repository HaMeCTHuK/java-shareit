package ru.practicum.shareit.item.mapper;

import org.mapstruct.*;
import ru.practicum.shareit.booking.entity.BookingEntity;
import ru.practicum.shareit.item.entity.ItemEntity;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.mapper.UserMapper;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ItemRepositoryMapper {

    @Mapping(target = "lastBooking", ignore = true)
    @Mapping(target = "nextBooking", ignore = true)
    Item toItem(ItemEntity itemEntity);

    ItemEntity toEntity(Item item);

    @Mapping(target = "id", ignore = true)
    void updateEntity(Item item, @MappingTarget ItemEntity itemEntity);

    @Mapping(target = "start", source = "start", qualifiedByName = "toLocalDateTime")
    @Mapping(target = "end", source = "end", qualifiedByName = "toLocalDateTime")
    @Mapping(target = "itemId", source = "item.id")
    @Mapping(target = "bookerId", source = "booker.id")
    Item.ItemBooking toItemBooking(BookingEntity bookingEntity);

    @Named("toLocalDateTime")
    default LocalDateTime toLocalDateTime(Timestamp timestamp) {
        return timestamp.toLocalDateTime();
    }

    @Named("toTimeStamp")
    default Timestamp toTimeStamp(LocalDateTime localDateTime) {
        return Timestamp.valueOf(localDateTime);
    }
}

