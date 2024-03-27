package ru.practicum.shareit.booking.entity;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.entity.ItemEntity;
import ru.practicum.shareit.user.entity.UserEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(schema = "public", name = "bookings")
public class BookingEntity implements Serializable {

    private static final long serialVersionUID = 139483743275040L;

    @Id
    @SequenceGenerator(name = "pk_sequence", schema = "public", sequenceName = "bookings_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pk_sequence")
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    private Long id;  //уникальный идентификатор бронирования;

    @Column(name = "start_date", nullable = false)
    private Timestamp start;  //дата и время начала бронирования;

    @Column(name = "end_time", nullable = false)
    private Timestamp end;  //дата и время конца бронирования;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "item_id", nullable = false)
    private ItemEntity item;  //вещь, которую пользователь бронирует;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "booker_id", nullable = false)
    private UserEntity booker;  //пользователь, который осуществляет бронирование;

    @Column(name = "status", nullable = false)
    private BookingStatus status;  //статус бронирования.

}
