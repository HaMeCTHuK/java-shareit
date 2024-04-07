package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testCreateItem() throws Exception {
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/items")
                .header("X-Sharer-User-Id", 1L)
                .contentType("application/json")
                .content("{\"name\": \"Test Item\"}"));

        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateItem() throws Exception {
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.patch("/items/1")
                .header("X-Sharer-User-Id", 1L)
                .contentType("application/json")
                .content("{\"name\": \"Updated Item\"}"));

        resultActions.andExpect(status().isNotFound());
    }

    @Test
    public void testGetItemWithUserId() throws Exception {
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/items/1")
                .header("X-Sharer-User-Id", 1L)
                .contentType("application/json"));

        resultActions.andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteItemById() throws Exception {
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.delete("/items/1"));

        resultActions.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Данные не найдены Item не найден"));
    }


    @Test
    public void testGetAllItemsWithUserId() throws Exception {
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/items")
                .header("X-Sharer-User-Id", 1L)
                .contentType("application/json"));

        resultActions.andExpect(status().isNotFound());
    }

    @Test
    public void testSearchItemsByText() throws Exception {
        // Mocking the request and sending it to the controller
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/items/search")
                .header("X-Sharer-User-Id", 1L)
                .param("text", "test")
                .contentType("application/json"));

        resultActions.andExpect(status().is5xxServerError());
    }

    @Test
    public void testCreateComment() throws Exception {
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/items/1/comment")
                .header("X-Sharer-User-Id", 1L)
                .contentType("application/json")
                .content("{\"text\": \"Test comment\"}"));

        resultActions.andExpect(status().is5xxServerError());
    }
}
