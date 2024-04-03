package ru.practicum.shareit.request.entity;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.user.entity.UserEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;


@Getter
@Setter
@Entity
@Table(schema = "public", name = "requests")
public class ItemRequestEntity implements Serializable {

    private static final long serialVersionUID = 109234603764023L;

    @Id
    @SequenceGenerator(name = "pk_sequence", schema = "public", sequenceName = "requests_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pk_sequence")
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    private Long id;

    @Column(name = "description", nullable = false)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requestor_id", nullable = false)
    private UserEntity requestor;

    @Column(name = "created", nullable = false)
    private Timestamp created;
}
