package ru.practicum.shareit.item.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.entity.CommentEntity;
import ru.practicum.shareit.item.entity.ItemEntity;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.entity.UserEntity;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CommentMapperTest {

    private final CommentMapper mapper = Mappers.getMapper(CommentMapper.class);
    private CommentEntity commentEntity;
    private ItemEntity itemEntity;
    private Comment comment;
    private CommentDto commentDto;
    private UserEntity userEntity;
    private UserEntity userEntity2;

    @BeforeEach
    void setUp() {
        userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setEmail("test@mail.com");
        userEntity.setName("Lesha");

        Set<CommentEntity> commentEntitySet = new HashSet<>();
        commentEntitySet.add(commentEntity);

        userEntity2 = new UserEntity();
        userEntity2.setId(1L);
        userEntity2.setEmail("test2@mail.com");
        userEntity2.setName("Lesha2");
        userEntity2.setComments(commentEntitySet);

        itemEntity = new ItemEntity();
        itemEntity.setId(1L);
        itemEntity.setOwner(userEntity2);
        itemEntity.setAvailable(true);
        itemEntity.setDescription("trtrtr");

        commentEntity = new CommentEntity();
        commentEntity.setId(1L);
        commentEntity.setText("tratatat");
        commentEntity.setCreated(Timestamp.valueOf(LocalDateTime.now()));
        commentEntity.setItem(itemEntity);
        commentEntity.setAuthor(userEntity);

        itemEntity = new ItemEntity();
        itemEntity.setId(1L);
        itemEntity.setAvailable(true);
        itemEntity.setDescription("trtrtr");

        comment = new Comment();
        comment.setAuthorName("asda");
        comment.setId(1L);
        comment.setText("comment");
        comment.setItemId(1L);
        comment.setAuthorId(1L);
        comment.setCreated(LocalDateTime.now());

        commentDto = new CommentDto();
        commentDto.setAuthorName("asda");
        commentDto.setId(1L);
        commentDto.setText("comment");
        commentDto.setItemId(1L);
        commentDto.setAuthorId(1L);
        commentDto.setCreated(LocalDateTime.now());
    }

    @Test
    public void testToItemComment() {
        Item.ItemComment itemComment = mapper.toItemComment(commentEntity);

        assertNotNull(itemComment);
        assertEquals(commentEntity.getItem().getId(), itemComment.getItemId());
        assertEquals(commentEntity.getAuthor().getId(), itemComment.getAuthorId());
        assertEquals(commentEntity.getCreated().toLocalDateTime(), itemComment.getCreated());
    }

    @Test
    public void testToCommentWithIds() {
        Comment comment = mapper.toCommentWithIds(commentDto, 1L, 1L);

        assertNotNull(comment);
        assertEquals(commentDto.getText(), comment.getText());
        assertEquals(1L, comment.getItemId());
        assertEquals(1L, comment.getAuthorId());
    }

    @Test
    public void testToComment() {
        Comment comment = mapper.toComment(commentDto);

        assertNotNull(comment);
        assertEquals(commentDto.getText(), comment.getText());
    }

    @Test
    public void testToCommentEntity() {
        CommentEntity commentEntity = mapper.toCommentEntity(comment);

        assertNotNull(commentEntity);
        assertEquals(comment.getText(), commentEntity.getText());
        assertEquals(comment.getCreated(), commentEntity.getCreated().toLocalDateTime());
    }

    @Test
    public void testToCommentDto() {
        CommentDto commentDto = mapper.toCommentDto(commentEntity);

        assertNotNull(commentDto);
        assertEquals(commentEntity.getText(), commentDto.getText());
        assertEquals(commentEntity.getAuthor().getId(), commentDto.getAuthorId());
        assertEquals(commentEntity.getItem().getId(), commentDto.getItemId());
    }
}
