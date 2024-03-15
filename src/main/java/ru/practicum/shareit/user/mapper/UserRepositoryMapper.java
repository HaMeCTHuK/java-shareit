package ru.practicum.shareit.user.mapper;

import org.mapstruct.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.entity.UserEntity;
import ru.practicum.shareit.user.model.User;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserRepositoryMapper {

    @Mapping(target = "id", ignore = true)
    User createToUser(UserDto userDto);

    @Mapping(target = "id", ignore = true)
    UserEntity createToEntity(User user);

    @Mapping(target = "id", ignore = true)  /////
    @Named("toUserEntity")
    UserEntity toEntity(User user);

    User toUserFromEntity(UserEntity userEntity);

    @Mapping(target = "id", ignore = true)
    void updateEntity(User user, @MappingTarget UserEntity userEntity);

}
