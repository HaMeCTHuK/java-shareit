package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.exception.DataNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ItemController.class)
public class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;
    @MockBean
    private UserMapper userMapper;
    @MockBean
    private ItemService itemService;
    @MockBean
    private ItemMapper itemMapper;
    @MockBean
    private CommentMapper commentMapper;
    @InjectMocks
    private ItemController itemController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    public ItemControllerTest() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(itemController).build();
    }

    @Test
    public void testCreateItem() throws Exception {
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/items")
                .header("X-Sharer-User-Id", 1L)
                .contentType("application/json")
                .content("{\"name\": \"Test Item\"}"));

        resultActions.andExpect(status().is2xxSuccessful());
    }

    @Test
    public void testUpdateItem() throws Exception {
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.patch("/items/1")
                .header("X-Sharer-User-Id", 4L)
                .contentType("application/json")
                .content("{\"name\": \"Updated Item\"}"));

        resultActions.andExpect(status().is2xxSuccessful());
    }

    @Test
    public void testGetItemWithUserId() throws Exception {
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/items/1")
                .header("X-Sharer-User-Id", 1L)
                .contentType("application/json"));

        resultActions.andExpect(status().is2xxSuccessful());
    }

    @Test
    public void testDeleteItemById() throws Exception {
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.delete("/items/1"));

        resultActions.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Данные не найдены Пользователь с ID 1 не найден"));
    }


    @Test
    public void testGetAllItemsWithUserId() throws Exception {
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/items")
                .header("X-Sharer-User-Id", 1L)
                .contentType("application/json"));

        resultActions.andExpect(status().is2xxSuccessful());
    }

    @Test
    public void testSearchItemsByText() throws Exception {
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/items/search")
                .header("X-Sharer-User-Id", 1L)
                .param("text", "test")
                .contentType("application/json"));

        resultActions.andExpect(status().is2xxSuccessful());
    }

    @Test
    public void testCreateComment() throws Exception {
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/items/1/comment")
                .header("X-Sharer-User-Id", 1L)
                .contentType("application/json")
                .content("{\"text\": \"Test comment\"}"));

        resultActions.andExpect(status().is2xxSuccessful());
    }

    @Test
    void testCreateItem_ValidRequest_ReturnsSuccess() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setEmail("asdasd@asfasdf.com");
        userDto.setName("nameeee");

        ItemDto itemDto = new ItemDto();
        itemDto.setName("name");
        itemDto.setName("Test Item");
        itemDto.setAvailable(true);
        itemDto.setRequestId(1L);
        itemDto.setDescription("123124");
        itemDto.setOwner(userDto);

        when(itemMapper.toItemFromItemDtoCreate(itemDto, 1L)).thenReturn(new Item());
        when(itemService.createItem(new Item())).thenReturn(itemDto);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/items")
                .header("X-Sharer-User-Id", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"Test Item\", \"description\": \"Description of the test item\", \"available\": true}"));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Item"))
                .andExpect(jsonPath("$.description").value("123124"))
                .andExpect(jsonPath("$.available").value(true));

        verify(itemService, times(1)).createItem(any());
    }

    @Test
    void testDeleteItemById_ItemExists_ReturnsSuccess() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setEmail("asdasd@asfasdf.com");
        userDto.setName("nameeee");

        ItemDto itemDto = new ItemDto();
        itemDto.setName("name");
        itemDto.setName("Test Item");
        itemDto.setAvailable(true);
        itemDto.setRequestId(1L);
        itemDto.setDescription("123124");
        itemDto.setOwner(userDto);

        when(itemService.getItem(1L)).thenReturn(itemDto);
        doNothing().when(itemService).deleteItem(1L);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.delete("/items/1"));

        resultActions.andExpect(status().isOk());

        verify(itemService, times(1)).deleteItem(anyLong());
    }

    @Test
    void testDeleteItemById_ItemNotExists_ReturnsNotFound() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setEmail("asdasd@asfasdf.com");
        userDto.setName("nameeee");

        ItemDto itemDto = new ItemDto();
        itemDto.setName("name");
        itemDto.setName("Test Item");
        itemDto.setAvailable(true);
        itemDto.setRequestId(1L);
        itemDto.setDescription("123124");
        itemDto.setOwner(userDto);

        when(itemService.getItem(1L)).thenReturn(itemDto);
        doThrow(new DataNotFoundException("Данные не найдены Пользователь с ID 1 не найден")).when(itemService).deleteItem(1L);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.delete("/items/1"));

        resultActions.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Данные не найдены Данные не найдены Пользователь с ID 1 не найден"));

        verify(itemService, times(1)).deleteItem(1L);
    }
}
