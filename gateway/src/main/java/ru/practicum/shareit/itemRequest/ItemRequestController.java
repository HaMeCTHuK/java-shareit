package ru.practicum.shareit.itemRequest;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.itemRequest.dto.ItemRequestRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import static ru.practicum.shareit.common.Constants.X_SHARER_USER_ID;

@Controller
@RequestMapping(path = "/requests")
@AllArgsConstructor
@Slf4j
public class ItemRequestController {
    private final ItemRequestClient itemRequestClientClient;

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                 @Valid @RequestBody ItemRequestRequestDto itemRequestDto) {
        log.info("Пришел /POST запрос на создание запроса {} от пользователя с id {}", itemRequestDto, userId);
        ResponseEntity<Object> itemRequestResponseDto = itemRequestClientClient.create(userId, itemRequestDto);
        log.info("Ответ отправлен {}", itemRequestResponseDto);
        return itemRequestResponseDto;
    }

    @GetMapping
    public ResponseEntity<Object> getAllByUser(@RequestHeader(X_SHARER_USER_ID) Long userId) {
        log.info("Пришел /GET запрос на получение списка запросов от пользователя с id {}", userId);
        ResponseEntity<Object> itemRequestResponseDtos = itemRequestClientClient.getAllByUser(userId);
        log.info("Ответ отправлен {}", itemRequestResponseDtos);
        return itemRequestResponseDtos;
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAll(@PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                               @Positive @RequestParam(defaultValue = "10") int size,
                                               @RequestHeader(X_SHARER_USER_ID) Long userId) {
        log.info("Пришел /GET запрос на получение списка запросов от пользователя с id {} созданных другим пользователем",
                userId);
        ResponseEntity<Object> itemRequestResponseDtos = itemRequestClientClient.getAll(userId, from, size);
        log.info("Ответ отправлен {}", itemRequestResponseDtos);
        return itemRequestResponseDtos;
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getById(@PathVariable Long requestId,
                                          @RequestHeader(X_SHARER_USER_ID) Long userId) {
        log.info("Пришел /GET запрос на получение запроса по id");
        ResponseEntity<Object> itemRequestResponseDto = itemRequestClientClient.getById(requestId, userId);
        log.info("Ответ отправлен {}", itemRequestResponseDto);
        return itemRequestResponseDto;
    }
}
