package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.exception.DataNotFoundException;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.mapper.ItemRepositoryMapper;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.entity.ItemRequestEntity;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.entity.UserEntity;
import ru.practicum.shareit.user.mapper.UserRepositoryMapper;
import ru.practicum.shareit.user.repository.UserRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ItemRequestServiceTest {

    @Mock
    private ItemRequestRepository itemRequestRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private ItemRequestMapper itemRequestMapper;
    @Mock
    private UserRepositoryMapper userRepositoryMapper;
    @Mock
    private ItemRepositoryMapper itemRepositoryMapper;
    @Mock
    private ItemMapper itemMapper;

    @InjectMocks
    private ItemRequestServiceImpl itemRequestService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createRequest_Successful() {
        Long userId = 1L;
        UserEntity userEntity = new UserEntity();
        userEntity.setId(userId);
        userEntity.setName("John Doe");
        userEntity.setEmail("john.doe@example.com");

        UserDto userDto = new UserDto();
        userDto.setId(userId);
        userDto.setName("John");
        userDto.setEmail("john@test.com");

        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setCreated(LocalDateTime.now());
        itemRequestDto.setRequestor(userDto);
        itemRequestDto.setDescription("123");
        itemRequestDto.setItems(Collections.emptyList());

        ItemRequestEntity itemRequestEntity = new ItemRequestEntity();
        itemRequestEntity.setCreated(Timestamp.valueOf(LocalDateTime.now()));
        itemRequestEntity.setDescription("123");
        itemRequestEntity.setRequestor(userEntity);


        when(userRepository.existsById(userId)).thenReturn(true);
        when(userRepository.getReferenceById(userId)).thenReturn(userEntity);
        when(itemRequestMapper.toItemRequest(itemRequestDto)).thenReturn(new ItemRequest());
        when(itemRequestMapper.toEntity(any(ItemRequest.class))).thenReturn(itemRequestEntity);
        when(itemRequestRepository.save(itemRequestEntity)).thenReturn(itemRequestEntity);
        when(itemRequestMapper.toDto(itemRequestEntity)).thenReturn(itemRequestDto);

        ItemRequestDto createdRequest = itemRequestService.createRequest(itemRequestDto, userId);

        assertNotNull(createdRequest);
        verify(itemRequestRepository).save(itemRequestEntity);
    }

    @Test
    void getOwnRequests_Successful() {
        Long userId = 1L;
        when(userRepository.existsById(userId)).thenReturn(true);
        List<ItemRequestEntity> requestEntities = List.of(new ItemRequestEntity());
        when(itemRequestRepository.findByRequestorId(userId)).thenReturn(requestEntities);
        when(itemRequestMapper.toDto(any(ItemRequestEntity.class))).thenReturn(new ItemRequestDto());

        List<ItemRequestDto> result = itemRequestService.getOwnRequests(userId);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(itemRequestRepository).findByRequestorId(userId);
    }

    @Test
    void getAllRequestsByOtherUsers_Successful() {
        Long userId = 1L;
        int from = 0;
        int size = 5;
        Pageable pageable = PageRequest.of(from, size);
        when(userRepository.existsById(userId)).thenReturn(true);
        Page<ItemRequestEntity> page = new PageImpl<>(List.of(new ItemRequestEntity()));
        when(itemRequestRepository.findByRequestorIdNot(eq(userId), any(Pageable.class))).thenReturn(page);
        when(itemRequestMapper.toDto(any(ItemRequestEntity.class))).thenReturn(new ItemRequestDto());

        List<ItemRequestDto> result = itemRequestService.getAllRequestsByOtherUsers(userId, from, size, pageable);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(itemRequestRepository).findByRequestorIdNot(eq(userId), any(Pageable.class));
    }

    @Test
    void getRequestById_Successful() {
        Long requestId = 1L;
        Long userId = 1L;
        when(userRepository.existsById(userId)).thenReturn(true);
        ItemRequestEntity requestEntity = new ItemRequestEntity();
        when(itemRequestRepository.findById(requestId)).thenReturn(Optional.of(requestEntity));
        when(itemRequestMapper.toDto(any(ItemRequestEntity.class))).thenReturn(new ItemRequestDto());

        ItemRequestDto result = itemRequestService.getRequestById(requestId, userId);

        assertNotNull(result);
        verify(itemRequestRepository).findById(requestId);
    }

    @Test
    void getRequestById_NotFound() {
        Long requestId = 1L;
        Long userId = 1L;
        when(userRepository.existsById(userId)).thenReturn(true);
        when(itemRequestRepository.findById(requestId)).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> itemRequestService.getRequestById(requestId, userId));
    }
}
