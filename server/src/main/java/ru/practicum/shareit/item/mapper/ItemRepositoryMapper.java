package ru.practicum.shareit.item.mapper;

import org.mapstruct.*;
import ru.practicum.shareit.booking.entity.BookingEntity;
import ru.practicum.shareit.item.entity.ItemEntity;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemResponseOnRequestDto;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ItemRepositoryMapper {

    @Mapping(target = "lastBooking", ignore = true)
    @Mapping(target = "nextBooking", ignore = true)
    Item toItem(ItemEntity itemEntity);

    @Mapping(target = "request", ignore = true)
    ItemEntity toEntity(Item item);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "request.created", ignore = true)
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

    @Mapping(target = "requestId", source = "request.id")
    ItemResponseOnRequestDto itemEntityToItemResponseOnRequestDto(ItemEntity itemEntity);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "ItemResponseOnRequestDto.requestId", source = "ItemEntity.request.id")
    @Mapping(target = "available", source = "available")
    List<ItemResponseOnRequestDto> toItemsResponseListFromEntity(List<ItemEntity> itemEntityList);
}

