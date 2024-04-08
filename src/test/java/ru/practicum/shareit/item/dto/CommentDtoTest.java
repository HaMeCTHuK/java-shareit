package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class CommentDtoTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JacksonTester<CommentDto> json;

    @Test
    void testSerializeCommentDto() throws Exception {
        String content = "{\"id\":1,\"text\":\"Test Comment\",\"itemId\":2,\"authorId\":3,\"authorName\":\"Test Author\",\"created\":\"2024-04-07T12:00:00\"}";
        CommentDto commentDto = CommentDto.builder()
                .id(1L)
                .text("Test Comment")
                .itemId(2L)
                .authorId(3L)
                .authorName("Test Author")
                .created(LocalDateTime.of(2024,4,7,12,0,0))
                .build();

        assertThat(this.json.write(commentDto)).isEqualToJson(content);
    }

    @Test
    void testDeserializeCommentDto() throws Exception {
        String content = "{\"id\":1,\"text\":\"Test Comment\",\"itemId\":2,\"authorId\":3,\"authorName\":\"Test Author\",\"created\":\"2024-04-07T12:00:00\"}";
        CommentDto commentDto = CommentDto.builder()
                .id(1L)
                .text("Test Comment")
                .itemId(2L)
                .authorId(3L)
                .authorName("Test Author")
                .created(LocalDateTime.now())
                .build();

        assertThat(this.json.parse(content)).isEqualTo(commentDto);
    }
}
