package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@JsonTest
public class ItemDtoTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testSerializeItemDto() throws Exception {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDateTime now = LocalDateTime.parse(LocalDateTime.now().format(formatter));

        Item.ItemBooking lastBooking = new Item.ItemBooking(1L, now.minusDays(1), now, 1L, 2L, BookingStatus.APPROVED);
        Item.ItemBooking nextBooking = new Item.ItemBooking(2L, now.plusDays(1), now.plusDays(2), 1L, 3L, BookingStatus.WAITING);
        Item.ItemComment comment = new Item.ItemComment(1L, "Test comment", 1L, 2L, "John Doe", now);
        UserDto userDto = UserDto.builder().id(1L).email("owner@example.com").name("Owner Name").build();
        ItemDto itemDto = ItemDto.builder()
                .id(1L)
                .name("Test Item")
                .description("Test Description")
                .available(true)
                .owner(userDto)
                .requestId(1L)
                .lastBooking(lastBooking)
                .nextBooking(nextBooking)
                .comments(Collections.singletonList(comment))
                .build();

        String expectedJson = "{\"id\":1,\"name\":\"Test Item\",\"description\":\"Test Description\"," +
                "\"available\":true,\"owner\":{\"id\":1,\"name\":\"Owner Name\",\"email\":\"owner@example.com\"}," +
                "\"requestId\":1,\"lastBooking\":{\"id\":1,\"start\":\"" + now.minusDays(1).format(formatter) + "\"," +
                "\"end\":\"" + now.format(formatter) + "\",\"itemId\":1,\"bookerId\":2,\"status\":\"APPROVED\"}," +
                "\"nextBooking\":{\"id\":2,\"start\":\"" + now.plusDays(1).format(formatter) + "\"," +
                "\"end\":\"" + now.plusDays(2).format(formatter) + "\",\"itemId\":1,\"bookerId\":3,\"status\":\"WAITING\"}," +
                "\"comments\":[{\"id\":1,\"text\":\"Test comment\",\"itemId\":1,\"authorId\":2,\"authorName\":\"John Doe\"," +
                "\"created\":\"" + now.format(formatter) + "\"}]}";

        String actualJson = objectMapper.writeValueAsString(itemDto);

        JSONAssert.assertEquals(expectedJson, actualJson, true);
    }

    @Test
    void testDeserializeItemDto() throws Exception {
        String json = "{\"id\":1,\"name\":\"Test Item\",\"description\":\"Test Description\"," +
                "\"available\":true,\"owner\":{\"id\":1,\"name\":\"Owner Name\",\"email\":\"owner@example.com\"}," +
                "\"requestId\":1,\"lastBooking\":{\"id\":1,\"start\":\"2022-01-01T12:00:00\",\"end\":\"2022-01-02T12:00:00\"," +
                "\"itemId\":1,\"bookerId\":2,\"status\":\"APPROVED\"}," +
                "\"nextBooking\":{\"id\":2,\"start\":\"2022-01-03T12:00:00\",\"end\":\"2022-01-04T12:00:00\"," +
                "\"itemId\":1,\"bookerId\":3,\"status\":\"WAITING\"}," +
                "\"comments\":[{\"id\":1,\"text\":\"Test comment\",\"itemId\":1,\"authorId\":2,\"authorName\":\"John Doe\"," +
                "\"created\":\"2022-01-01T12:00:00\"}]}";

        ItemDto itemDto = objectMapper.readValue(json, ItemDto.class);

        assertEquals(1L, itemDto.getId());
        assertEquals("Test Item", itemDto.getName());
        assertEquals("Test Description", itemDto.getDescription());
        assertTrue(itemDto.getAvailable());
        assertEquals(1L, itemDto.getOwner().getId());
        assertEquals("Owner Name", itemDto.getOwner().getName());
        assertEquals("owner@example.com", itemDto.getOwner().getEmail());
        assertEquals(1L, itemDto.getRequestId());
        assertEquals(1L, itemDto.getLastBooking().getId());
        assertEquals(2L, itemDto.getLastBooking().getBookerId());
        assertEquals(BookingStatus.APPROVED, itemDto.getLastBooking().getStatus());
        assertEquals(1, itemDto.getComments().size());
        assertEquals("Test comment", itemDto.getComments().get(0).getText());
    }
}
