package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.entity.UserEntity;
import ru.practicum.shareit.user.mapper.UserRepositoryMapper;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class ItemRequestServiceImplTest {

    @Mock
    private ItemRequestRepository itemRequestRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserRepositoryMapper userRepositoryMapper;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private ItemRepositoryMapper itemRepositoryMapper;

    @Mock
    private ItemMapper itemMapper;

    @Mock
    private ItemRequestMapper itemRequestMapper;

    @InjectMocks
    private ItemRequestServiceImpl itemRequestService;

    @Test
    void createRequest_ok() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setName("John Doe");
        userEntity.setEmail("john.doe@example.com");

        UserDto requestor = new UserDto();
        requestor.setId(1L);

        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setRequestor(requestor);

        ItemRequestEntity itemRequestEntity = new ItemRequestEntity();
        itemRequestEntity.setId(1L);

        when(userRepository.existsById(1L)).thenReturn(true);
        when(userRepository.getReferenceById(1L)).thenReturn(userEntity);
        when(itemRequestMapper.toItemRequest(itemRequestDto)).thenReturn(new ItemRequest());
        when(itemRequestMapper.toEntity(any(ItemRequest.class))).thenReturn(itemRequestEntity);
        when(itemRequestRepository.save(any(ItemRequestEntity.class))).thenReturn(itemRequestEntity);
        when(itemRequestMapper.toDto(itemRequestEntity)).thenReturn(itemRequestDto);

        ItemRequestDto savedRequestDto = itemRequestService.createRequest(itemRequestDto, 1L);

        assertNotNull(savedRequestDto);
        assertEquals(itemRequestDto, savedRequestDto);
    }


    @Test
    void getOwnRequests() {
        Long userId = 1L;
        UserEntity userEntity = new UserEntity();
        userEntity.setId(userId);
        when(userRepository.existsById(userId)).thenReturn(true);
        when(itemRequestRepository.findByRequestorId(userId)).thenReturn(Collections.singletonList(new ItemRequestEntity()));
        when(itemRepository.findAllByRequestIds(anyList())).thenReturn(Collections.singletonList(new ItemEntity()));
        when(itemRepositoryMapper.toItemsResponseListFromEntity(anyList())).thenReturn(Collections.singletonList(new ItemResponseOnRequestDto()));
        when(itemRequestMapper.toDto(any(ItemRequestEntity.class))).thenReturn(new ItemRequestDto());

        List<ItemRequestDto> ownRequests = itemRequestService.getOwnRequests(userId);

        assertNotNull(ownRequests);
        assertEquals(1, ownRequests.size());
    }

    @Test
    void getAllRequestsByOtherUsers() {
        Long userId = 1L;
        Pageable pageable = mock(Pageable.class);

        Page<ItemRequestEntity> emptyPage = Page.empty();
        when(userRepository.existsById(userId)).thenReturn(true);
        when(itemRequestRepository.findByRequestorIdNot(userId, pageable)).thenReturn(emptyPage);

        when(itemRepository.findAllByRequestIds(anyList())).thenReturn(Collections.emptyList());
        when(itemRepositoryMapper.toItemsResponseListFromEntity(Collections.emptyList())).thenReturn(Collections.singletonList(new ItemResponseOnRequestDto()));
        when(itemRequestMapper.toDto(any(ItemRequestEntity.class))).thenReturn(new ItemRequestDto());

        List<ItemRequestDto> requestsByOtherUsers = itemRequestService.getAllRequestsByOtherUsers(userId, 0, 10, pageable);

        assertNotNull(requestsByOtherUsers);
        assertEquals(0, requestsByOtherUsers.size());
    }


    @Test
    void getRequestById() {
        Long requestId = 1L;
        Long userId = 1L;

        ItemRequestEntity requestEntity = new ItemRequestEntity();
        ItemResponseOnRequestDto itemResponseOnRequestDto = new ItemResponseOnRequestDto();
        itemResponseOnRequestDto.setRequestId(1L);
        List<ItemResponseOnRequestDto> items = new ArrayList<>();
        items.add(itemResponseOnRequestDto);
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setItems(items);

        requestEntity.setId(requestId);
        when(itemRequestRepository.findById(requestId)).thenReturn(Optional.of(requestEntity));
        when(userRepository.existsById(userId)).thenReturn(true);

        ItemEntity itemEntity = new ItemEntity();
        itemEntity.setRequest(requestEntity);
        when(itemRepository.findAllByRequestId(requestId)).thenReturn(Collections.singletonList(itemEntity));
        when(itemRepositoryMapper.toItemsResponseListFromEntity(Collections.singletonList(itemEntity))).thenReturn(items);
        when(itemRequestMapper.toDto(requestEntity)).thenReturn(itemRequestDto);

        ItemRequestDto requestDto = itemRequestService.getRequestById(requestId, userId);

        assertNotNull(requestDto);
        assertEquals(requestDto.getItems().get(0).getRequestId(), requestId);
    }


    @Test
    void getAllRequestsByOtherUsers_NotFound() {
        Long userId = 999L;
        Pageable pageable = mock(Pageable.class);
        when(userRepository.existsById(userId)).thenReturn(false);

        assertThrows(DataNotFoundException.class, () -> itemRequestService.getAllRequestsByOtherUsers(userId, 0, 10, pageable));
    }


    @Test
    void getRequestById_InvalidReuestId() {
        Long requestId = 999L;
        Long userId = 1L;
        when(itemRequestRepository.findById(requestId)).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> itemRequestService.getRequestById(requestId, userId));
    }

    @Test
    void getRequestById_InvalidUserId() {
        Long requestId = 1L;
        Long userId = 999L;
        ItemRequestEntity requestEntity = new ItemRequestEntity();
        requestEntity.setId(requestId);
        when(itemRequestRepository.findById(requestId)).thenReturn(Optional.of(requestEntity));
        when(userRepository.existsById(userId)).thenReturn(false);

        assertThrows(DataNotFoundException.class, () -> itemRequestService.getRequestById(requestId, userId));
    }



}
