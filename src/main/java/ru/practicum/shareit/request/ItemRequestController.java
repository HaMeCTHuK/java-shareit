package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.common.FromSizeRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import javax.validation.Valid;
import java.util.List;

import static ru.practicum.shareit.common.Constants.X_SHARER_USER_ID;

@RestController
@RequestMapping(path = "/requests")
@Slf4j
@RequiredArgsConstructor
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDto createRequest(@RequestHeader(X_SHARER_USER_ID) Long userId, @Valid @RequestBody ItemRequestDto itemRequestDto) {
        log.info("Добавить новый запрос вещи");
       return itemRequestService.createRequest(itemRequestDto, userId);
    }

    @GetMapping
    public List<ItemRequestDto> getOwnRequests(@RequestHeader(X_SHARER_USER_ID) Long userId) {
        log.info("Получаем список своих запросов вместе с данными об ответах на них.");
        return itemRequestService.getOwnRequests(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAllRequestsByOtherUsers(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "10") Integer size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "created");
        final Pageable pageable = FromSizeRequest.of(from, size, sort);
        log.info("Получаем список запросов, созданных другими пользователями.");
        return itemRequestService.getAllRequestsByOtherUsers(userId, from, size, pageable);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getRequestById(@PathVariable Long requestId,
                                         @RequestHeader(X_SHARER_USER_ID) Long userId) {
        log.info("Получаем данные об запросе по id");
        return itemRequestService.getRequestById(requestId, userId);
    }
}
