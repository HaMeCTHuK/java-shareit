package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DataNotFoundException;
import ru.practicum.shareit.item.entity.ItemEntity;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.mapper.ItemRepositoryMapper;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemResponseOnRequestDto;
import ru.practicum.shareit.request.entity.ItemRequestEntity;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.entity.UserEntity;
import ru.practicum.shareit.user.mapper.UserRepositoryMapper;
import ru.practicum.shareit.user.repository.UserRepository;

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
    private final ItemRepositoryMapper itemRepositoryMapper;
    private final ItemMapper itemMapper;

    @Override
    public ItemRequestDto createRequest(ItemRequestDto itemRequestDto, Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new DataNotFoundException("User не найден");
        }
        UserEntity userEntity = userRepository.getReferenceById(userId);

        ItemRequest itemRequest = itemRequestMapper.toItemRequest(itemRequestDto);
        itemRequest.setRequestor(userRepositoryMapper.toUserFromEntity(userEntity));
        itemRequest.setCreated(LocalDateTime.now());

        ItemRequestEntity requestEntity = itemRequestMapper.toEntity(itemRequest);
        ItemRequestEntity savedRequest = itemRequestRepository.save(requestEntity);

        return itemRequestMapper.toDto(savedRequest);
    }

    @Override
    public List<ItemRequestDto> getOwnRequests(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new DataNotFoundException("User не найден");
        }
        List<ItemRequestEntity> userRequests = itemRequestRepository.findByRequestorId(userId);

        List<Long> idList = userRequests.stream()
                .map(ItemRequestEntity::getId)
                .collect(Collectors.toList());

        List<ItemEntity> itemEntityList = itemRepository.findAllByRequestIds(idList);
        List<ItemResponseOnRequestDto> items = itemRepositoryMapper.toItemsResponseListFromEntity(itemEntityList);

        List<ItemRequestDto> requestDtos = userRequests.stream()
                .map(itemRequestMapper::toDto)
                .collect(Collectors.toList());

        for (ItemRequestDto requestDto : requestDtos) {
            List<ItemResponseOnRequestDto> filteredItems = items.stream()
                    .filter(item -> {
                        Long requestId = item.getRequestId();
                        return requestId != null && requestId.equals(requestDto.getId());
                    })
                    .collect(Collectors.toList());
            requestDto.setItems(filteredItems);
        }

        return requestDtos;
    }

    @Override
    public List<ItemRequestDto> getAllRequestsByOtherUsers(Long userId, int from, int size, Pageable pageable) {
        if (!userRepository.existsById(userId)) {
            throw new DataNotFoundException("User не найден");
        }
        Page<ItemRequestEntity> otherUserRequestsPage = itemRequestRepository.findByRequestorIdNot(userId, pageable);

        List<Long> idList = otherUserRequestsPage.stream()
                .map(ItemRequestEntity::getId)
                .collect(Collectors.toList());

        List<ItemEntity> itemEntityList = itemRepository.findAllByRequestIds(idList);
        List<ItemResponseOnRequestDto> items = itemRepositoryMapper.toItemsResponseListFromEntity(itemEntityList);


        List<ItemRequestDto> recivedList = otherUserRequestsPage.getContent().stream()
                .map(itemRequestMapper::toDto)
                .collect(Collectors.toList());

        for (ItemRequestDto requestDto : recivedList) {
            List<ItemResponseOnRequestDto> filteredItems = items.stream()
                    .filter(item -> {
                        Long requestId = item.getRequestId();
                        return requestId != null && requestId.equals(requestDto.getId());
                    })
                    .collect(Collectors.toList());
            requestDto.setItems(filteredItems);
        }

        return recivedList;
    }


    @Override
    public ItemRequestDto getRequestById(Long requestId, Long userId) {
        ItemRequestEntity requestEntity = itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new DataNotFoundException("Item request не найден"));

        if (!userRepository.existsById(userId)) {
            throw new DataNotFoundException("User не найден");
        }

        List<ItemEntity> itemEntityList = itemRepository.findAllByRequestId(requestEntity.getId());
        List<ItemResponseOnRequestDto> items = itemRepositoryMapper.toItemsResponseListFromEntity(itemEntityList);

            List<ItemResponseOnRequestDto> filteredItems = items.stream()
                    .filter(item -> {
                        Long reqId = item.getRequestId();
                        return reqId != null && reqId.equals(requestEntity.getId());
                    })
                    .collect(Collectors.toList());

            ItemRequestDto itemRequestDto = itemRequestMapper.toDto(requestEntity);

            itemRequestDto.setItems(filteredItems);


        return itemRequestDto;
    }
}
