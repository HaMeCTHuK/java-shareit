package ru.practicum.shareit.user.entity;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.entity.CommentEntity;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserEntityTest {

    @Test
    void testGettersAndSetters() {
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");

        assertEquals(1L, user.getId());
        assertEquals("John Doe", user.getName());
        assertEquals("john.doe@example.com", user.getEmail());
    }

    @Test
    void testComments() {
        UserEntity user = new UserEntity();
        user.setId(1L);

        CommentEntity comment1 = new CommentEntity();
        comment1.setId(1L);
        CommentEntity comment2 = new CommentEntity();
        comment2.setId(2L);

        Set<CommentEntity> comments = new HashSet<>();
        comments.add(comment1);
        comments.add(comment2);
        user.setComments(comments);

        assertEquals(2, user.getComments().size());
    }

}