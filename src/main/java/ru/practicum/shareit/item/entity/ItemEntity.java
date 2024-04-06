package ru.practicum.shareit.item.entity;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.request.entity.ItemRequestEntity;
import ru.practicum.shareit.user.entity.UserEntity;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(schema = "public", name = "items")
public class ItemEntity implements Serializable {

    private static final long serialVersionUID = 619163495558764L;

    @Id
    @SequenceGenerator(name = "pk_sequence", schema = "public", sequenceName = "items_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pk_sequence")
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "is_available", nullable = false)
    private Boolean available;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private UserEntity owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id")
    private ItemRequestEntity request;

}
