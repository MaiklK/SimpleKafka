package ru.maiklk.microtwo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.maiklk.microtwo.dto.impl.MessageDto;
import ru.maiklk.microtwo.dto.impl.TelegramUserDto;
import ru.maiklk.microtwo.kafka.ProducerService;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProducerController.class)
@DisplayName("Тесты ProducerController через MockMvc")
class ProducerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProducerService producerService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("POST /sendTelegramUser - успешная отправка TelegramUser")
    void sendTelegramUserAndReturnOk() throws Exception {
        var dto = TelegramUserDto.builder()
                .id(1L)
                .userName("test_user")
                .firstName("Test")
                .lastName("User")
                .languageCode("ru")
                .build();

        mockMvc.perform(post("/sendTelegramUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().string("TelegramUser send"));

        verify(producerService, times(1)).send(dto);
    }

    @Test
    @DisplayName("POST /sendMessage - успешная отправка Message")
    void sendMessageAndReturnOk() throws Exception {
        var dto = MessageDto.builder()
                .chatId(1L)
                .date(123456789)
                .messageId(1)
                .text("Hello world")
                .build();

        mockMvc.perform(post("/sendMessage")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().string("Message send"));

        verify(producerService, times(1)).send(dto);
    }

    @Test
    @DisplayName("POST /sendTelegramUser - невалидный JSON возвращает 400 Bad Request")
    void sendTelegramUserInvalidJsonReturnsBadRequest() throws Exception {
        String invalidJson = "{invalid}";

        mockMvc.perform(post("/sendTelegramUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /sendTelegramUser - пустое тело запроса возвращает 400 Bad Request")
    void sendTelegramUserEmptyBodyReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/sendTelegramUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /sendMessage - невалидный JSON возвращает 400 Bad Request")
    void sendMessageInvalidJsonReturnsBadRequest() throws Exception {
        String invalidJson = "{invalid}";

        mockMvc.perform(post("/sendMessage")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /sendMessage - пустое тело запроса возвращает 400 Bad Request")
    void sendMessageEmptyBodyReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/sendMessage")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isBadRequest());
    }
}