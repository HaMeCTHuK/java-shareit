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
    Item toItemFromItemDtoCreate(ItemDto itemDto, Long userId);  //ItemCreateRequest

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "owner.id", source = "userId")
    @Mapping(target = "lastBooking", ignore = true)
    @Mapping(target = "nextBooking" , ignore = true)
    Item toItemDtoFromUpdateRequest(ItemDto itemDto, Long userId);  //ItemUpdateRequest  @Mapping(target = "request", ignore = true)

    List<Item> toItemsList(List<ItemDto> itemDtos);

    List<ItemDto> toItemsDtoList(List<Item> items);

    Item itemDtoToItem(ItemDto itemDto);

    ItemDto toItemDto(Item item);

    ItemDto.ItemBooking map(Item.ItemBooking booking);

    ItemDto.ItemComment map(Item.ItemComment comment);

/*    @Mapping(target = "itemId", source = "item.id")
    @Mapping(target = "authorId", source = "author.id")
    @Mapping(target = "authorName", source = "author.name")
    CommentDto toCommentDto(Comment comment);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "text", source = "itemDto.text")
    @Mapping(target = "item.id", source = "itemId")
    @Mapping(target = "author.id", source = "authorId")
    Comment toComment(CommentDto commentDto, Long itemId, Long authorId);*/


}
