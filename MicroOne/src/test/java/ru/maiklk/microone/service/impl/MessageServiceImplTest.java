package ru.maiklk.microone.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.maiklk.microone.entity.Message;
import ru.maiklk.microone.repository.MessageRepo;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Testcontainers
@DisplayName("Интеграционные тесты MessageServiceImpl")
class MessageServiceImplTest {
    @Autowired
    private MessageServiceImpl messageService;

    @Autowired
    private MessageRepo messageRepo;

    private Message message1;
    private Message message2;
    private Message message3;

    @BeforeEach
    void setUp() {
        messageRepo.deleteAllInBatch();
        message1 = buildMessage(111111111L, 1640995200, 1, "First message");
        message2 = buildMessage(222222222L, 1640995300, 2, "Second message");
        message3 = buildMessage(333333333L, 1640995400, 3, "Third message");
    }

    @Test
    @DisplayName("Сохранение валидного сообщения")
    void testSaveEntity_Success() {
        messageService.saveEntity(message1);

        List<Message> savedMessages = messageRepo.findAll();
        assertEquals(1, savedMessages.size());

        Message savedMessage = savedMessages.getFirst();
        assertEquals(message1.getChatId(), savedMessage.getChatId());
        assertEquals(message1.getDate(), savedMessage.getDate());
        assertEquals(message1.getMessageId(), savedMessage.getMessageId());
        assertEquals(message1.getText(), savedMessage.getText());
    }

    @Test
    @DisplayName("Сохранение null сообщения не должно ничего сохранять")
    void testSaveEntity_NullMessage() {
        messageService.saveEntity(null);
        assertEquals(0, messageRepo.count());
    }

    @Test
    @DisplayName("Сохранение нескольких сообщений")
    void testSaveEntity_MultipleMessages() {
        messageService.saveEntity(message1);
        messageService.saveEntity(message2);
        messageService.saveEntity(message3);

        List<Message> savedMessages = messageRepo.findAll();
        assertEquals(3, savedMessages.size());

        assertTrue(savedMessages.stream().anyMatch(m -> m.getChatId() == 111111111L && m.getText().equals("First message")));
        assertTrue(savedMessages.stream().anyMatch(m -> m.getChatId() == 222222222L && m.getText().equals("Second message")));
        assertTrue(savedMessages.stream().anyMatch(m -> m.getChatId() == 333333333L && m.getText().equals("Third message")));
    }

    @ParameterizedTest(name = "Параметризованный тест сохранения сообщений: {0}")
    @MethodSource("edgeCaseMessagesProvider")
    @DisplayName("Параметризованный тест сохранения сообщений")
    void testSaveEntity_EdgeCases(Message message) {
        messageService.saveEntity(message);
        List<Message> savedMessages = messageRepo.findAll();
        assertEquals(1, savedMessages.size());
        Message savedMessage = savedMessages.getFirst();

        assertEquals(message.getChatId(), savedMessage.getChatId());
        assertEquals(message.getDate(), savedMessage.getDate());
        assertEquals(message.getMessageId(), savedMessage.getMessageId());

        if (message.getText() != null && message.getText().length() > 200) {
            assertEquals(message.getText().substring(0, 200), savedMessage.getText());
        } else {
            assertEquals(message.getText(), savedMessage.getText());
        }
    }

    private static Message buildMessage(long chatId, int date, int messageId, String text) {
        return Message.builder()
                .chatId(chatId)
                .date(date)
                .messageId(messageId)
                .text(text)
                .build();
    }

    static Stream<Message> edgeCaseMessagesProvider() {
        return Stream.of(
                buildMessage(999999999L, 1640995500, 99, "Message with special chars: !@#$%^&*()_+-=[]{}|;':\",./<>?"),
                buildMessage(888888888L, 1640995600, 88, ""),
                buildMessage(777777777L, 1640995700, 77, "A".repeat(1000)),
                buildMessage(0L, 0, 0, "Zero values message"),
                buildMessage(-123456789L, -1640995200, -1, "Negative values message")
        );
    }
}