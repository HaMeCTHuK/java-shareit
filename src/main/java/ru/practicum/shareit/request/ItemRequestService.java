package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemResponseOnRequestDto;

import org.springframework.data.domain.Pageable;
import java.util.List;

public interface ItemRequestService {

    ItemRequestDto createRequest(ItemRequestDto itemRequestDto, Long userId);

    List<ItemRequestDto> getOwnRequests(Long userId);

    List<ItemRequestDto> getAllRequestsByOtherUsers(Long userId, int from, int size, Pageable pageable);

    ItemRequestDto getRequestById(Long requestId);
}
