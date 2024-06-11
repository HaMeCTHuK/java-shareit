package ru.practicum.shareit.item.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@EqualsAndHashCode(of = {"id"})
@RequiredArgsConstructor
public class CommentDto {

    private  Long id;
    private  String text;
    private  Long itemId;
    private  Long authorId;
    private  String authorName;
    private LocalDateTime created;

}
