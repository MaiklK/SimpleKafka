package ru.maiklk.microone.kafka;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.maiklk.microone.dto.impl.MessageDto;
import ru.maiklk.microone.dto.impl.TelegramUserDto;
import ru.maiklk.microone.entity.Message;
import ru.maiklk.microone.entity.TelegramUser;
import ru.maiklk.microone.exception.KafkaMessageProcessingException;
import ru.maiklk.microone.service.impl.MessageServiceImpl;
import ru.maiklk.microone.service.impl.TelegramUserServiceImpl;
import ru.maiklk.microone.util.ConverterDto;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("Тесты для ConsumerService")
class ConsumerServiceTest {

    @InjectMocks
    ConsumerService consumerService;

    @Mock
    ConverterDto converterDto;

    @Mock
    TelegramUserServiceImpl telegramUserServiceImpl;

    @Mock
    MessageServiceImpl messageServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Успешная обработка пользователя Telegram")
    void shouldProcessTelegramUserSuccessfully() {
        String message = "{\"id\":11111111111,\"firstName\":\"Test\",\"lastName\":\"User\",\"userName\":\"test\",\"languageCode\":\"ru\"}";
        TelegramUserDto dto = TelegramUserDto.builder()
                .id(11111111111L)
                .languageCode("ru")
                .firstName("Test")
                .userName("test")
                .lastName("User")
                .build();

        when(converterDto.parseTelegramUserDto(message)).thenReturn(dto);
        when(converterDto.mapToTelegramUser(dto)).thenReturn(new TelegramUser());

        consumerService.sendTelegramUser(message);

        verify(converterDto).parseTelegramUserDto(message);
        verify(converterDto).mapToTelegramUser(dto);
        verify(telegramUserServiceImpl).saveEntity(any());
    }

    @Test
    @DisplayName("Ошибка при обработке пользователя Telegram приводит к DLT и выбрасыванию KafkaMessageProcessingException")
    void shouldCallDltAndThrowExceptionWhenTelegramUserProcessingFails() {
        String message = "invalid-message";
        when(converterDto.parseTelegramUserDto(message)).thenThrow(new RuntimeException("Parse error"));

        assertThatThrownBy(() -> consumerService.sendTelegramUser(message))
                .isInstanceOf(KafkaMessageProcessingException.class)
                .hasMessageContaining("Parse error");

        verify(converterDto).parseTelegramUserDto(message);
        verifyNoMoreInteractions(converterDto, telegramUserServiceImpl);
    }

    @Test
    @DisplayName("Успешная обработка сообщения")
    void shouldProcessMessageSuccessfully() {
        String message = "{\"id\":123,\"text\":\"hello\"}";
        MessageDto dto = MessageDto.builder()
                .chatId(123L)
                .date(123)
                .messageId(123)
                .text("hello")
                .build();

        when(converterDto.parseMessageDto(message)).thenReturn(dto);
        when(converterDto.mapToMessage(dto)).thenReturn(new Message());

        consumerService.sendMessage(message);

        verify(converterDto).parseMessageDto(message);
        verify(converterDto).mapToMessage(dto);
        verify(messageServiceImpl).saveEntity(any());
    }

    @Test
    @DisplayName("Ошибка при обработке сообщения приводит к DLT и выбрасыванию KafkaMessageProcessingException")
    void shouldCallDltAndThrowExceptionWhenMessageProcessingFails() {
        String message = "invalid-message";
        when(converterDto.parseMessageDto(message)).thenThrow(new RuntimeException("Parse error"));

        assertThatThrownBy(() -> consumerService.sendMessage(message))
                .isInstanceOf(KafkaMessageProcessingException.class)
                .hasMessageContaining("Parse error");

        verify(converterDto).parseMessageDto(message);
        verifyNoMoreInteractions(converterDto, messageServiceImpl);
    }
}