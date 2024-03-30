package ru.practicum.shareit.booking.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.entity.BookingEntity;
import ru.practicum.shareit.item.entity.ItemEntity;
import ru.practicum.shareit.user.entity.UserEntity;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<BookingEntity, Long> {

    @Query("select b from BookingEntity b where b.item.owner = :owner and b.start > :now order by b.start desc ")
    List<BookingEntity> findFutureByOwnerItems(@Param("owner")UserEntity owner, @Param("now")Timestamp start);

    @Query("select b from BookingEntity b where b.item.owner = :owner and b.status = :status order by b.start desc ")
    List<BookingEntity> findAllByOwnerItemsAndStatus(
            @Param("owner") UserEntity owner,
            @Param("status") BookingStatus status); //BookingEntity

    @Query("SELECT b FROM BookingEntity b JOIN FETCH b.item JOIN FETCH b.booker WHERE b.id = :bookingId")
    Optional<BookingEntity> findByIdWithItemAndBooker(@Param("bookingId") Long bookingId);


    Optional<BookingEntity> findFirstByItemAndStartBeforeOrderByStartDesc(ItemEntity itemEntity, Timestamp start);

    Optional<BookingEntity> findFirstByItemAndStartAfterOrderByStart(ItemEntity itemEntity, Timestamp start);

    List<BookingEntity> findAllByItemAndBooker(ItemEntity itemEntity, UserEntity booker);

    @Query("SELECT b FROM BookingEntity b WHERE b.booker.id = :userId AND b.status = :status ORDER BY b.id DESC")
    List<BookingEntity> findAllByBookerIdAndStatusSortedByIdDesc(@Param("userId") Long userId, @Param("status") BookingStatus status);


    List<BookingEntity> findAllByBookerIdOrderByIdDesc(Long userId);

    @Query("SELECT b FROM BookingEntity b WHERE b.booker = :user AND b.start > :now ORDER BY b.start DESC")
    List<BookingEntity> findFutureByBooker(@Param("user") UserEntity user, @Param("now")Timestamp start);

    List<BookingEntity> findAllByItemOwnerOrderByIdDesc(UserEntity owner);

    //boolean existByItemAndBookerAndStatus(ItemEntity itemEntity, UserEntity booker, BookingStatus status);

}
