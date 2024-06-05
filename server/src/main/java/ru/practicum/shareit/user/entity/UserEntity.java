package ru.practicum.shareit.user.entity;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.item.entity.CommentEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(schema = "public", name = "users")
public class UserEntity implements Serializable {

    private static final long serialVersionUID = 63516854941321L;

    @Id
    @SequenceGenerator(name = "pk_sequence", schema = "public", sequenceName = "users_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pk_sequence")
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    //связь
    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    private Set<CommentEntity> comments;
}
