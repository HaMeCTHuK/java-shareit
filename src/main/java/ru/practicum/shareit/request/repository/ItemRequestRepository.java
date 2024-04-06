package ru.practicum.shareit.request.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.entity.ItemEntity;
import ru.practicum.shareit.request.entity.ItemRequestEntity;


import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequestEntity, Long> {
    List<ItemRequestEntity> findByRequestorId(Long userId);

    Page<ItemRequestEntity> findByRequestorIdNot(Long userId, Pageable pageable);
}
