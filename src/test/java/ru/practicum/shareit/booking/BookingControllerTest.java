package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.practicum.shareit.booking.mapper.BookingMapper;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(BookingController.class)
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingService bookingService;
    @MockBean
    private BookingMapper bookingMapper;

    @Test
    void testCreateBooking() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"itemId\": 1, \"startDate\": \"2024-04-06T10:00:00\", \"endDate\": \"2024-04-06T12:00:00\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetBooking() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/bookings/7")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void testGetOwnerBookings() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", "APPROVED")
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void testGetAllUserBookings() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void testUpdateBooking() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch("/bookings/9")
                        .header("X-Sharer-User-Id", 1L)
                        .param("approved", "true"))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void testDeleteBooking() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/bookings/11"))
                .andExpect(status().is2xxSuccessful());
    }
}
