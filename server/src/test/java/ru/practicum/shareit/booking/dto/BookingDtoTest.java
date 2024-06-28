package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.booking.BookingStatus;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class BookingDtoTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JacksonTester<BookingDto> json;

    @Test
    public void testSerializeBookingDto() throws IOException {
        BookingDto bookingDto = BookingDto.builder()
                .id(1L)
                .start(LocalDateTime.of(2024, 4, 10, 10, 0))
                .end(LocalDateTime.of(2024, 4, 12, 12, 0))
                .status(BookingStatus.APPROVED)
                .build();

        assertThat(objectMapper.writeValueAsString(bookingDto))
                .isEqualTo(json.write(bookingDto).getJson());
    }

    @Test
    public void testDeserializeBookingDto() throws IOException {
        String jsonContent = "{\"id\":1,\"start\":\"2024-04-10T10:00:00\",\"end\":\"2024-04-12T12:00:00\",\"status\":\"APPROVED\"}";

        BookingDto expectedBookingDto = BookingDto.builder()
                .id(1L)
                .start(LocalDateTime.of(2024, 4, 10, 10, 0))
                .end(LocalDateTime.of(2024, 4, 12, 12, 0))
                .status(BookingStatus.APPROVED)
                .build();

        assertThat(json.parseObject(jsonContent))
                .isEqualTo(expectedBookingDto);
    }
}
