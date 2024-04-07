package ru.practicum.shareit.request.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.junit.jupiter.api.Assertions.assertEquals;

@JsonTest
@SpringJUnitConfig
class ItemResponseOnRequestDtoTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testSerializeItemResponseOnRequestDto() throws Exception {
        ItemResponseOnRequestDto dto = new ItemResponseOnRequestDto();
        dto.setId(1L);
        dto.setName("Test Item");
        dto.setDescription("This is a test item");
        dto.setRequestId(100L);
        dto.setAvailable(true);

        String json = objectMapper.writeValueAsString(dto);

        assertEquals("{\"id\":1,\"name\":\"Test Item\",\"description\":\"This is a test item\",\"requestId\":100,\"available\":true}", json);
    }

    @Test
    void testDeserializeItemResponseOnRequestDto() throws Exception {
        String json = "{\"id\":1,\"name\":\"Test Item\",\"description\":\"This is a test item\",\"requestId\":100,\"available\":true}";

        ItemResponseOnRequestDto dto = objectMapper.readValue(json, ItemResponseOnRequestDto.class);

        assertEquals(1L, dto.getId());
        assertEquals("Test Item", dto.getName());
        assertEquals("This is a test item", dto.getDescription());
        assertEquals(100L, dto.getRequestId());
        assertEquals(true, dto.getAvailable());
    }
}
