package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;

public class ItemRepository {

    private final HashMap<Item, User> users = new HashMap<>();
    private Long generatedId = 0L;

    public Item save(Item item) {
        return null;
    }

}
