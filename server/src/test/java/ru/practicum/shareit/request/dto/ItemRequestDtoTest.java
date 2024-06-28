package ru.practicum.shareit.request.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@JsonTest
@SpringJUnitConfig
class ItemRequestDtoTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testSerializeItemRequestDto() throws Exception {
        UserDto requestor = UserDto.builder()
                .id(1L)
                .name("John Doe")
                .email("john.doe@example.com")
                .build();

        ItemRequestDto itemRequestDto = ItemRequestDto.builder()
                .id(1L)
                .description("Test description")
                .created(LocalDateTime.of(2022, 1, 1, 12, 0, 0))
                .requestor(requestor)
                .build();

        String json = objectMapper.writeValueAsString(itemRequestDto);

        assertEquals("{\"id\":1,\"description\":\"Test description\",\"requestor\":{\"id\":1,\"name\":\"John Doe\",\"email\":\"john.doe@example.com\"},\"created\":\"2022-01-01T12:00:00\",\"items\":null}", json);
    }

    @Test
    void testDeserializeItemRequestDto() throws Exception {
        String json = "{\"id\":1,\"description\":\"Test description\",\"created\":\"2022-01-01T12:00:00\",\"items\":null}";

        ItemRequestDto itemRequestDto = objectMapper.readValue(json, ItemRequestDto.class);

        assertEquals(1L, itemRequestDto.getId());
        assertEquals("Test description", itemRequestDto.getDescription());
    }
}
