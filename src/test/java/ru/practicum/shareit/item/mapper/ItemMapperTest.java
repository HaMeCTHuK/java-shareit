package ru.practicum.shareit.item.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.MockitoAnnotations;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.mapper.UserMapper;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

public class ItemMapperTest {

    private final ItemMapper mapper = Mappers.getMapper(ItemMapper.class);
    private ItemDto itemDto;
    private Item item;

    @BeforeEach
    void setUp() {
        itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("Test Item");
        itemDto.setDescription("Test Description");
        itemDto.setRequestId(123L);

        item = new Item();
        item.setId(1L);
        item.setName("Test Item");
        item.setDescription("Test Description");
        item.setRequest(new ItemRequest());
    }

    @Test
    public void testToItemFromItemDtoCreate() {
        Item mappedItem = mapper.toItemFromItemDtoCreate(itemDto, 1L);

        assertNotNull(mappedItem);
        assertEquals(itemDto.getId(), mappedItem.getId());
        assertEquals(itemDto.getName(), mappedItem.getName());
        assertEquals(itemDto.getDescription(), mappedItem.getDescription());
        assertEquals(itemDto.getRequestId(), mappedItem.getRequest().getId());
        assertEquals(1L, mappedItem.getOwner().getId());
    }

    @Test
    public void testToItemDtoFromUpdateRequest() {
        Item mappedItem = mapper.toItemDtoFromUpdateRequest(itemDto, 1L);

        assertNotNull(mappedItem);
        assertEquals(itemDto.getName(), mappedItem.getName());
        assertEquals(itemDto.getDescription(), mappedItem.getDescription());
        assertEquals(1L, mappedItem.getOwner().getId());
    }
/*    @Test
    public void testToItemsList() {
        List<ItemDto> itemDtoList = new ArrayList<>();
        itemDtoList.add(itemDto);

        List<Item> itemList = mapper.toItemsList(itemDtoList);

        assertNotNull(itemList);
        assertEquals(1, itemList.size());

        Item mappedItem = itemList.get(0);
        assertEquals(itemDto.getId(), mappedItem.getId());
        assertEquals(itemDto.getName(), mappedItem.getName());
        assertEquals(itemDto.getDescription(), mappedItem.getDescription());
        assertEquals(itemDto.getRequestId(), mappedItem.getRequest().getId());
    }

    @Test
    public void testToItemsDtoList() {
        List<Item> itemList = new ArrayList<>();
        itemList.add(item);

        List<ItemDto> itemDtoList = mapper.toItemsDtoList(itemList);

        assertNotNull(itemDtoList);
        assertEquals(1, itemDtoList.size());

        ItemDto mappedDto = itemDtoList.get(0);
        assertEquals(item.getId(), mappedDto.getId());
        assertEquals(item.getName(), mappedDto.getName());
        assertEquals(item.getDescription(), mappedDto.getDescription());
        assertEquals(item.getRequest().getId(), mappedDto.getRequestId());
    }

    @Test
    public void testItemDtoToItem() {
        Item mappedItem = mapper.itemDtoToItem(itemDto);

        assertNotNull(mappedItem);
        assertEquals(itemDto.getId(), mappedItem.getId());
        assertEquals(itemDto.getName(), mappedItem.getName());
        assertEquals(itemDto.getDescription(), mappedItem.getDescription());
        assertEquals(itemDto.getRequestId(), mappedItem.getRequest().getId());
    }

    @Test
    public void testToItemDto() {
        ItemDto mappedDto = mapper.toItemDto(item);

        assertNotNull(mappedDto);
        assertEquals(item.getId(), mappedDto.getId());
        assertEquals(item.getName(), mappedDto.getName());
        assertEquals(item.getDescription(), mappedDto.getDescription());
        assertEquals(item.getRequest().getId(), mappedDto.getRequestId());
    }*/

}
