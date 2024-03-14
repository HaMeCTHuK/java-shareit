package ru.practicum.shareit.item.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.entity.ItemEntity;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.entity.UserEntity;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.security.Timestamp;
import java.time.LocalDate;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = UserMapper.class)
public interface ItemMapper {

    @Mapping(target = "owner.id", source = "userId")
    @Mapping(target = "lastBooking", ignore = true)
    @Mapping(target = "nextBooking", ignore = true)
    Item toItemWithId(ItemDto itemDto, Long userId);  //ItemCreateRequest

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "request", ignore = true)
    @Mapping(target = "owner.id", source = "userId")
    @Mapping(target = "lastBooking", ignore = true)
    @Mapping(target = "nextBooking" , ignore = true)
    Item toItemWithoutId(ItemDto itemDto, Long userId);  //ItemUpdateRequest

    Item itemDtoToItem(ItemDto itemDto);

    ItemDto toItemDto(Item item);

    ItemDto.ItemBooking map(Item.ItemBooking booking);

    ItemDto.ItemComment map(Item.ItemComment comment);

   /* @Mapping(target = "itemId", source = "item.id")
    @Mapping(target = "authorId", source = "author.id")
    @Mapping(target = "authorName", source = "author.name")
    CommentResponse toResponse(Comment comment);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "text", source = "request.text")
    @Mapping(target = "item.id", source = "itemId")
    @Mapping(target = "author.id", source = "authorId")
    Comment toComment(CommentCreateRequest request,Long itemId, Long authorId);*/

    ItemEntity ItemToEntity(Item item);

    Item toItemFromEntity(ItemEntity itemEntity);

    @Mapping(target = "id", ignore = true)
    void updateEntity(Item item, @MappingTarget ItemEntity itemEntity);

}
