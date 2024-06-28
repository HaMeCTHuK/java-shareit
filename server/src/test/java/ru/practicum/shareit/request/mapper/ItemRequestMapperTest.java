package ru.practicum.shareit.request.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemResponseOnRequestDto;
import ru.practicum.shareit.request.entity.ItemRequestEntity;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

public class ItemRequestMapperTest {

    @InjectMocks
    private ItemRequestMapper itemRequestMapper = Mappers.getMapper(ItemRequestMapper.class);

    @Mock
    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testToEntity() {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(1L);
        itemRequest.setDescription("Test Description");
        itemRequest.setCreated(LocalDateTime.now());

        ItemRequestEntity itemRequestEntity = itemRequestMapper.toEntity(itemRequest);

        assertNotNull(itemRequestEntity);
        assertEquals(itemRequest.getDescription(), itemRequestEntity.getDescription());
        assertEquals(Timestamp.valueOf(itemRequest.getCreated()), itemRequestEntity.getCreated());
    }

    @Test
    void testToDto() {
        ItemRequestEntity itemRequestEntity = new ItemRequestEntity();
        itemRequestEntity.setId(1L);
        itemRequestEntity.setDescription("Test Description");
        itemRequestEntity.setCreated(Timestamp.valueOf(LocalDateTime.now()));

        ItemRequestDto itemRequestDto = itemRequestMapper.toDto(itemRequestEntity);

        assertNotNull(itemRequestDto);
        assertEquals(itemRequestEntity.getDescription(), itemRequestDto.getDescription());
        assertEquals(itemRequestEntity.getCreated().toLocalDateTime(), itemRequestDto.getCreated());
    }

    @Test
    void testToResponseDto() {
        ItemRequestEntity itemRequestEntity = new ItemRequestEntity();
        itemRequestEntity.setId(1L);
        itemRequestEntity.setDescription("Test Description");

        ItemResponseOnRequestDto responseDto = itemRequestMapper.toResponseDto(itemRequestEntity);

        assertNotNull(responseDto);
        assertEquals(itemRequestEntity.getId(), responseDto.getId());
        assertEquals(itemRequestEntity.getDescription(), responseDto.getDescription());
    }

    @Test
    void testToFromEntityToRequest() {
        ItemRequestEntity itemRequestEntity = new ItemRequestEntity();
        itemRequestEntity.setId(1L);
        itemRequestEntity.setDescription("Test Description");
        itemRequestEntity.setCreated(Timestamp.valueOf(LocalDateTime.now()));

        ItemRequest itemRequest = itemRequestMapper.toFromEntityToRequest(itemRequestEntity);

        assertNotNull(itemRequest);
        assertEquals(itemRequestEntity.getDescription(), itemRequest.getDescription());
        assertEquals(itemRequestEntity.getCreated().toLocalDateTime(), itemRequest.getCreated());
    }

    @Test
    void testToItemRequest() {
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setId(1L);
        itemRequestDto.setDescription("Test Description");
        itemRequestDto.setCreated(LocalDateTime.now());

        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setName("name");
        userDto.setEmail("test@mail.ru");

        User user = new User();
        user.setId(1L);
        user.setName("name");
        user.setEmail("test@mail.ru");


        when(userMapper.toUser(userDto)).thenReturn(user);
        ItemRequest itemRequest = itemRequestMapper.toItemRequest(itemRequestDto);

        assertNotNull(itemRequest);
        assertEquals(itemRequestDto.getDescription(), itemRequest.getDescription());
        assertEquals(itemRequestDto.getCreated(), itemRequest.getCreated());
    }

    @Test
    void testToDtoFromRequest() {
        ItemRequest itemRequest1 = new ItemRequest();
        itemRequest1.setId(1L);
        itemRequest1.setDescription("Test 1");
        itemRequest1.setCreated(LocalDateTime.now());

        ItemRequest itemRequest2 = new ItemRequest();
        itemRequest2.setId(2L);
        itemRequest2.setDescription("Test 2");
        itemRequest2.setCreated(LocalDateTime.now().minusDays(1));

        List<ItemRequestDto> itemRequestDtos = itemRequestMapper.toDtoFromRequest(List.of(itemRequest1, itemRequest2));

        assertNotNull(itemRequestDtos);
        assertEquals(2, itemRequestDtos.size());
        assertEquals(itemRequest1.getDescription(), itemRequestDtos.get(0).getDescription());
        assertEquals(itemRequest2.getDescription(), itemRequestDtos.get(1).getDescription());
    }

    @Test
    void testToLocalDateTime() {
        Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
        LocalDateTime localDateTime = itemRequestMapper.toLocalDateTime(timestamp);

        assertNotNull(localDateTime);
        assertEquals(timestamp.toLocalDateTime(), localDateTime);
    }

    @Test
    void testToTimeStamp() {
        LocalDateTime localDateTime = LocalDateTime.now();
        Timestamp timestamp = itemRequestMapper.toTimeStamp(localDateTime);

        assertNotNull(timestamp);
        assertEquals(Timestamp.valueOf(localDateTime), timestamp);
    }
}
