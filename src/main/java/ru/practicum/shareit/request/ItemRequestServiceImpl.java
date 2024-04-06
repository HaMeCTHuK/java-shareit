package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DataNotFoundException;
import ru.practicum.shareit.item.ItemServiceImpl;
import ru.practicum.shareit.item.entity.ItemEntity;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemResponseOnRequestDto;
import ru.practicum.shareit.request.entity.ItemRequestEntity;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.entity.UserEntity;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.mapper.UserRepositoryMapper;
import ru.practicum.shareit.user.repository.UserRepository;


import org.springframework.data.domain.Pageable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;
    private final ItemRequestMapper itemRequestMapper;
    private final UserRepository userRepository;
    private final UserRepositoryMapper userRepositoryMapper;
    private final ItemRepository itemRepository;

    @Override
    public ItemRequestDto createRequest(ItemRequestDto itemRequestDto, Long userId) {
        // Map ItemRequestDto to ItemRequestEntity
        if (!userRepository.existsById(userId)) {
            throw new DataNotFoundException("User не найден");
        }
        UserEntity userEntity = userRepository.getReferenceById(userId);

        ItemRequest itemRequest = itemRequestMapper.toItemRequest(itemRequestDto);
        itemRequest.setRequestor(userRepositoryMapper.toUserFromEntity(userEntity));
        itemRequest.setCreated(LocalDateTime.now());

        ItemRequestEntity requestEntity = itemRequestMapper.toEntity(itemRequest);

        //List<ItemEntity> itemEntityList = itemRepository.findAllByOwnerOrderById(userEntity);

        ItemRequestEntity savedRequest = itemRequestRepository.save(requestEntity);

        return itemRequestMapper.toDto(savedRequest);
    }

    @Override
    public List<ItemRequestDto> getOwnRequests(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new DataNotFoundException("User не найден");
        }
        List<ItemRequestEntity> userRequests = itemRequestRepository.findByRequestorId(userId);
        return userRequests.stream()
                .map(itemRequestMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemRequestDto> getAllRequestsByOtherUsers(Long userId, int from, int size, Pageable pageable) {
        // Get all requests except those created by the user
        Page<ItemRequestEntity> otherUserRequestsPage = itemRequestRepository.findByRequestorIdNot(userId, pageable);
        // Map each request to ItemRequestDto
        List<ItemRequest> recivedList = otherUserRequestsPage.getContent().stream()
                .map(itemRequestMapper::toFromEntityToRequest)
                .collect(Collectors.toList());
        return itemRequestMapper.toDtoFromRequest(recivedList);
    }


    @Override
    public ItemRequestDto getRequestById(Long requestId) {
        // Get the request by ID from the repository
        ItemRequestEntity requestEntity = itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Item request не найден"));
        // Map the request to ItemRequestDto
        return itemRequestMapper.toDto(requestEntity);
    }
}
