package ru.practicum.shareit.user.mapper;

import org.mapstruct.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.entity.UserEntity;
import ru.practicum.shareit.user.model.User;

//@Mapper(componentModel = "spring")
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    User toUser(UserDto userDto);

    UserDto toUserDto(User user);

    UserEntity toEntity(User user);

    User toUserFromEntity(UserEntity userEntity);

    @Mapping(target = "id", ignore = true)
    void updateEntity(User user, @MappingTarget UserEntity userEntity);
}
