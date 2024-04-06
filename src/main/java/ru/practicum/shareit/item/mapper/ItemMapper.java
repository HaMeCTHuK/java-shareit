package ru.practicum.shareit.item.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.mapper.UserMapper;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = UserMapper.class)
public interface ItemMapper {

    @Mapping(target = "owner.id", source = "userId")
    @Mapping(target = "lastBooking", ignore = true)
    @Mapping(target = "nextBooking", ignore = true)
    @Mapping(target = "request.id", source = "itemDto.requestId")
    Item toItemFromItemDtoCreate(ItemDto itemDto, Long userId);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "owner.id", source = "userId")
    @Mapping(target = "lastBooking", ignore = true)
    Item toItemDtoFromUpdateRequest(ItemDto itemDto, Long userId);

    List<Item> toItemsList(List<ItemDto> itemDtos);

    List<ItemDto> toItemsDtoList(List<Item> items);

    Item itemDtoToItem(ItemDto itemDto);

    @Mapping(target = "requestId", source = "request.id")
    ItemDto toItemDto(Item item);

    ItemDto.ItemBooking map(Item.ItemBooking booking);

    ItemDto.ItemComment map(Item.ItemComment comment);

}
