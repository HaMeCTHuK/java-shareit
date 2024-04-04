package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.query.Page;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemResponseOnRequestDto;

import javax.validation.Valid;
import java.awt.print.Pageable;
import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@Slf4j
@RequiredArgsConstructor
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDto createRequest(@RequestHeader("X-Sharer-User-Id") Long userId, @Valid @RequestBody ItemRequestDto itemRequestDto) {
        log.info("Добавить новый запрос вещи");
       return itemRequestService.createRequest(itemRequestDto, userId);
    }

    @GetMapping
    public List<ItemResponseOnRequestDto> getOwnRequests(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Получаем список своих запросов вместе с данными об ответах на них.");
        return itemRequestService.getOwnRequests(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAllRequestsByOtherUsers(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam("from") int from,
            @RequestParam("size") int size,
            Pageable pageable) {
        log.info("Получаем список запросов, созданных другими пользователями.");
        return itemRequestService.getAllRequestsByOtherUsers(userId, from, size, pageable);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getRequestById(@PathVariable Long requestId) {
        log.info("Получаем данные об запросе по id");
        return itemRequestService.getRequestById(requestId);
    }
}
