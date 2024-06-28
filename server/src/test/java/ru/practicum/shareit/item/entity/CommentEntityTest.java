package ru.practicum.shareit.item.entity;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.practicum.shareit.user.entity.UserEntity;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class CommentEntityTest {

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void whenSaveComment_thenCommentIsSaved() {
        UserEntity user = new UserEntity();
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        entityManager.persist(user);

        ItemEntity item = new ItemEntity();
        item.setName("Drill");
        item.setDescription("Powerful electric drill");
        item.setAvailable(true);
        item.setOwner(user);
        entityManager.persist(item);

        CommentEntity comment = new CommentEntity();
        comment.setAuthor(user);
        comment.setItem(item);
        comment.setText("This is a test comment");
        comment.setCreated(Timestamp.valueOf(LocalDateTime.now()));
        CommentEntity savedComment = entityManager.persistFlushFind(comment);

        assertThat(savedComment).isNotNull();
        assertThat(savedComment.getId()).isNotNull();
        assertThat(savedComment.getText()).isEqualTo("This is a test comment");
    }

    @Test
    public void whenFindCommentById_thenCommentIsFound() {
        UserEntity user = new UserEntity();
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        entityManager.persist(user);

        ItemEntity item = new ItemEntity();
        item.setName("Drill");
        item.setDescription("Powerful electric drill");
        item.setAvailable(true);
        item.setOwner(user);
        entityManager.persist(item);

        CommentEntity comment = new CommentEntity();
        comment.setAuthor(user);
        comment.setItem(item);
        comment.setText("Test comment");
        comment.setCreated(Timestamp.valueOf(LocalDateTime.now()));
        CommentEntity savedComment = entityManager.persistFlushFind(comment);

        CommentEntity foundComment = entityManager.find(CommentEntity.class, savedComment.getId());

        assertThat(foundComment).isEqualTo(savedComment);
    }

    @Test
    public void whenUpdateComment_thenCommentIsUpdated() {
        UserEntity user = new UserEntity();
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        entityManager.persist(user);

        ItemEntity item = new ItemEntity();
        item.setName("Drill");
        item.setDescription("Powerful electric drill");
        item.setAvailable(true);
        item.setOwner(user);
        entityManager.persist(item);

        CommentEntity comment = new CommentEntity();
        comment.setAuthor(user);
        comment.setItem(item);
        comment.setText("Initial comment");
        comment.setCreated(Timestamp.valueOf(LocalDateTime.now()));
        CommentEntity savedComment = entityManager.persistFlushFind(comment);

        savedComment.setText("Updated comment");
        entityManager.persistAndFlush(savedComment);

        CommentEntity updatedComment = entityManager.find(CommentEntity.class, savedComment.getId());

        assertThat(updatedComment.getText()).isEqualTo("Updated comment");
    }

    @Test
    public void whenDeleteComment_thenCommentIsDeleted() {
        UserEntity user = new UserEntity();
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        entityManager.persist(user);

        ItemEntity item = new ItemEntity();
        item.setName("Drill");
        item.setDescription("Powerful electric drill");
        item.setAvailable(true);
        item.setOwner(user);
        entityManager.persist(item);

        CommentEntity comment = new CommentEntity();
        comment.setAuthor(user);
        comment.setItem(item);
        comment.setText("To be deleted");
        comment.setCreated(Timestamp.valueOf(LocalDateTime.now()));
        CommentEntity savedComment = entityManager.persistFlushFind(comment);

        entityManager.remove(savedComment);
        entityManager.flush();

        CommentEntity deletedComment = entityManager.find(CommentEntity.class, savedComment.getId());

        assertThat(deletedComment).isNull();
    }
}