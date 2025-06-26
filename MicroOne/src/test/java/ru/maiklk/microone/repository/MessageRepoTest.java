package ru.maiklk.microone.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.maiklk.microone.entity.Message;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
@DisplayName("Интеграционные тесты MessageRepo")
class MessageRepoTest {
    @Autowired
    private MessageRepo messageRepo;

    private Message message1;
    private Message message2;

    @BeforeEach
    void setUp() {
        messageRepo.deleteAllInBatch();
        message1 = messageRepo.save(buildMessage(111111111L, 1640995200, 1, "First message"));
        message2 = messageRepo.save(buildMessage(222222222L, 1640995300, 2, "Second message"));
    }

    @Test
    @DisplayName("Сохранение и поиск по id")
    void testSaveAndFindById() {
        var foundMessage = messageRepo.findById(message1.getId());

        assertTrue(foundMessage.isPresent());
        assertEquals(message1.getChatId(), foundMessage.get().getChatId());
        assertEquals(message1.getText(), foundMessage.get().getText());
    }

    @Test
    @DisplayName("Сохранение и поиск всех сообщений")
    void testSaveAndFindAll() {
        List<Message> allMessages = messageRepo.findAll();

        assertEquals(2, allMessages.size());
        assertTrue(allMessages.stream().anyMatch(m -> m.getChatId() == 111111111L));
        assertTrue(allMessages.stream().anyMatch(m -> m.getChatId() == 222222222L));
    }

    @Test
    @DisplayName("Удаление по id")
    void testDeleteById() {
        messageRepo.deleteById(message1.getId());
        Optional<Message> foundMessage = messageRepo.findById(message1.getId());

        assertFalse(foundMessage.isPresent());
    }

    @Test
    @DisplayName("Удаление всех сообщений")
    void testDeleteAll() {
        messageRepo.deleteAll();

        List<Message> allMessages = messageRepo.findAll();

        assertEquals(0, allMessages.size());
    }

    @Test
    @DisplayName("Проверка количества сообщений")
    void testCount() {
        long count = messageRepo.count();

        assertEquals(2, count);
    }

    @Test
    @DisplayName("Проверка существования по id")
    void testExistsById() {
        boolean exists = messageRepo.existsById(message1.getId());

        assertTrue(exists);
    }

    @Test
    @DisplayName("Проверка несуществующего id")
    void testExistsById_NotExists() {
        boolean exists = messageRepo.existsById(999999L);

        assertFalse(exists);
    }

    @Test
    @DisplayName("Массовое сохранение сообщений")
    void testSaveAll() {
        Message msgA = buildMessage(333333333L, 1640995400, 3, "Third message");
        Message msgB = buildMessage(444444444L, 1640995500, 4, "Fourth message");

        List<Message> messages = List.of(msgA, msgB);
        List<Message> savedMessages = messageRepo.saveAll(messages);

        assertEquals(2, savedMessages.size());
        assertEquals(4, messageRepo.count());
    }

    @Test
    @DisplayName("Поиск по списку id")
    void testFindAllById() {
        List<Long> ids = List.of(message1.getId(), message2.getId());
        List<Message> foundMessages = messageRepo.findAllById(ids);

        assertEquals(2, foundMessages.size());
        assertTrue(foundMessages.stream().anyMatch(m -> m.getChatId() == 111111111L));
        assertTrue(foundMessages.stream().anyMatch(m -> m.getChatId() == 222222222L));
    }

    @ParameterizedTest(name = "Сохранение сообщения: {0}")
    @MethodSource("edgeCaseMessagesProvider")
    @DisplayName("Параметризованный тест сохранения сообщений")
    void testSaveEdgeCaseMessages(Message message) {
        var saved = messageRepo.save(message);

        Optional<Message> found = messageRepo.findById(saved.getId());

        assertTrue(found.isPresent());
        assertEquals(message.getText(), found.get().getText());
    }

    static Stream<Message> edgeCaseMessagesProvider() {
        return Stream.of(
                buildMessage(0L, 0, 0, ""),
                buildMessage(-1L, -100, -5, "Negative values"),
                buildMessage(555555555L, 1640995600, 5, "Тест с русским текстом"),
                buildMessage(666666666L, 1640995700, 6, "Special chars !@#$%^&*()")
        );
    }

    private static Message buildMessage(long chatId, int date, int messageId, String text) {
        return Message.builder()
                .chatId(chatId)
                .date(date)
                .messageId(messageId)
                .text(text)
                .build();
    }
}