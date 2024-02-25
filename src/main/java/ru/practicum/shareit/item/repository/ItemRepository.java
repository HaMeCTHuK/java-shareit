package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class ItemRepository {

     private Long generatedId = 0L;

    private final HashMap<Long, Item> items = new HashMap<>();

    public Long getGenerateId () {
        return ++generatedId;
    }

    public Item save(Item item) {
        items.put(item.getId(), item);
        return item;
    }

    public Item get(Long itemId) {
        return items.get(itemId);
    }

    public void delete(Long itemId) {
        items.remove(itemId);
    }

    public List<Item> getAllItems() {
        return new ArrayList<>(items.values());
    }
}
