package ru.practicum.shareit.item.entity;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.user.entity.UserEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(schema = "public", name = "comments")
public class CommentEntity implements Serializable {

    private static final long serialVersionUID = 231324168498163L;

    @Id
    @SequenceGenerator(name = "pk_sequence", schema = "public", sequenceName = "comments_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pk_sequence")
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    private Long id;

    @Column(name = "text", nullable = false)
    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private UserEntity author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private ItemEntity item;

    @Column(name = "created", nullable = false)
    private Timestamp created;
}
