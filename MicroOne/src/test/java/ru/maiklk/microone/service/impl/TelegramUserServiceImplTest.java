package ru.maiklk.microone.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.maiklk.microone.entity.TelegramUser;
import ru.maiklk.microone.repository.TelegramUserRepo;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Testcontainers
@DisplayName("Интеграционные тесты TelegramUserServiceImpl")
class TelegramUserServiceImplTest {
    @Autowired
    private TelegramUserServiceImpl telegramUserService;

    @Autowired
    private TelegramUserRepo telegramUserRepo;

    @BeforeEach
    void setUp() {
        telegramUserRepo.deleteAllInBatch();
    }

    @Test
    @DisplayName("Сохранение валидного пользователя")
    void testSaveEntity_Success() {
        var user = buildUser(123456789L, "John", "Doe", "Joe", "en");
        telegramUserService.saveEntity(user);
        List<TelegramUser> savedUsers = telegramUserRepo.findAll();

        assertEquals(1, savedUsers.size());
        TelegramUser savedUser = savedUsers.getFirst();
        assertEquals(user.getId(), savedUser.getId());
        assertEquals(user.getFirstName(), savedUser.getFirstName());
        assertEquals(user.getLastName(), savedUser.getLastName());
        assertEquals(user.getUserName(), savedUser.getUserName());
        assertEquals(user.getLanguageCode(), savedUser.getLanguageCode());
    }

    @Test
    @DisplayName("Сохранение null пользователя не должно ничего сохранять")
    void testSaveEntity_nullEntity() {
        telegramUserService.saveEntity(null);
        assertEquals(0, telegramUserRepo.count());
    }

    @Test
    @DisplayName("Сохранение нескольких пользователей")
    void testSaveEntity_MultipleUsers() {
        var user1 = buildUser(111111111L, "Alice", "Smith", "Bella", "en");
        var user2 = buildUser(222222222L, "Bob", "Johnson", "Raids", "es");
        var user3 = buildUser(333333333L, "Charlie", "Brown", "Robinson", "fr");

        telegramUserService.saveEntity(user1);
        telegramUserService.saveEntity(user2);
        telegramUserService.saveEntity(user3);
        List<TelegramUser> savedUsers = telegramUserRepo.findAll();

        assertEquals(3, savedUsers.size());
        assertTrue(savedUsers.stream().anyMatch(u -> u.getId() == 111111111L && u.getUserName().equals("Bella")));
        assertTrue(savedUsers.stream().anyMatch(u -> u.getId() == 222222222L && u.getUserName().equals("Raids")));
        assertTrue(savedUsers.stream().anyMatch(u -> u.getId() == 333333333L && u.getUserName().equals("Robinson")));
    }

    @ParameterizedTest(name = "Параметризованный тест сохранения пользователя: {0}")
    @MethodSource("specialUsersProvider")
    void testSaveEntity_SpecialCases(TelegramUser user) {
        telegramUserService.saveEntity(user);
        List<TelegramUser> savedUsers = telegramUserRepo.findAll();

        assertEquals(1, savedUsers.size());
        TelegramUser savedUser = savedUsers.getFirst();
        assertEquals(user.getFirstName(), savedUser.getFirstName());
        assertEquals(user.getLastName(), savedUser.getLastName());
        assertEquals(user.getUserName(), savedUser.getUserName());
        assertEquals(user.getLanguageCode(), savedUser.getLanguageCode());
    }

    private static TelegramUser buildUser(long id, String firstName, String lastName, String userName, String languageCode) {
        return TelegramUser.builder()
                .id(id)
                .firstName(firstName)
                .lastName(lastName)
                .userName(userName)
                .languageCode(languageCode)
                .build();
    }

    private static Stream<TelegramUser> specialUsersProvider() {
        return Stream.of(
                buildUser(999999999L, "José-María", "O'Connor-Smith", "jose_maria_O'Connor", "es"),
                buildUser(888888888L, "", "", "", ""),
                buildUser(777777777L, "A".repeat(100), "A".repeat(100), "veryVeryLongUserName", "en"),
                buildUser(555555555L, "Test", "User", "testUser123", "en")
        );
    }
}