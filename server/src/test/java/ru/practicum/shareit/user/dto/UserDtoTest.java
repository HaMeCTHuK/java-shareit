package ru.practicum.shareit.user.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@JsonTest
class UserDtoTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testUserDtoSerialization() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setName("John Doe");
        userDto.setEmail("john.doe@example.com");

        String json = objectMapper.writeValueAsString(userDto);

        String expectedJson = "{\"id\":1,\"name\":\"John Doe\",\"email\":\"john.doe@example.com\"}";
        assertEquals(expectedJson, json);
    }

    @Test
    void testUserDtoDeserialization() throws Exception {
        String json = "{\"id\":1,\"name\":\"John Doe\",\"email\":\"john.doe@example.com\"}";

        UserDto userDto = objectMapper.readValue(json, UserDto.class);

        assertEquals(1L, userDto.getId());
        assertEquals("John Doe", userDto.getName());
        assertEquals("john.doe@example.com", userDto.getEmail());
    }
}
