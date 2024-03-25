package ru.practicum.shareit.item.mapper;

import org.mapstruct.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.entity.CommentEntity;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CommentMapper {
    Item.ItemComment toItemComment(CommentEntity commentEntity);

    Comment toCommentWithIds(CommentDto commentDto, Long itemId, Long userId);

    Comment toComment(CommentDto commentDto);

    @Mapping(target = "created", source = "created", qualifiedByName = "toTimeStamp")
    CommentEntity toCommentEntity(Comment comment);

    CommentDto toCommentDto(CommentEntity commentEntity);

    @Named("toLocalDateTime")
    default LocalDateTime toLocalDateTime(Timestamp timestamp) {
        return timestamp.toLocalDateTime();
    }

    @Named("toTimeStamp")
    default Timestamp toTimeStamp(LocalDateTime localDateTime) {
        return Timestamp.valueOf(localDateTime);
    }
}
