package ru.practicum.shareit.request.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemResponseOnRequestDto;
import ru.practicum.shareit.request.entity.ItemRequestEntity;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.mapper.UserMapper;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = UserMapper.class)
public interface ItemRequestMapper {

    @Mapping(target = "created", source = "created", qualifiedByName = "toTimeStamp")
    ItemRequestEntity toEntity(ItemRequest itemRequest);

    @Mapping(target = "created", source = "created", qualifiedByName = "toLocalDateTime")
    ItemRequestDto toDto(ItemRequestEntity savedRequest);

    @Mapping(target = "created", source = "created", qualifiedByName = "toLocalDateTime")
    @Mapping(target = "created", source = "created", qualifiedByName = "toLocalDateTime")
    ItemResponseOnRequestDto toResponseDto(ItemRequestEntity itemRequestEntity);

    @Mapping(target = "created", source = "created", qualifiedByName = "toLocalDateTime")
    ItemRequest toFromEntityToRequest(ItemRequestEntity itemRequestEntity);

    ItemRequest toItemRequest(ItemRequestDto itemRequestDto);

    List<ItemRequestDto> toDtoFromRequest(List<ItemRequest> recivedList);

    @Named("toLocalDateTime")
    default LocalDateTime toLocalDateTime(Timestamp timestamp) {
        return timestamp.toLocalDateTime();
    }

    @Named("toTimeStamp")
    default Timestamp toTimeStamp(LocalDateTime localDateTime) {
        return Timestamp.valueOf(localDateTime);
    }
}
