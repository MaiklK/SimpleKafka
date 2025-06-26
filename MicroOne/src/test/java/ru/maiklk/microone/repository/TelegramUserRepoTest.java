package ru.maiklk.microone.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.maiklk.microone.entity.TelegramUser;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
@DisplayName("Интеграционные тесты TelegramUserRepo")
class TelegramUserRepoTest {
    @Autowired
    private TelegramUserRepo telegramUserRepo;

    private TelegramUser telegramUser1;
    private TelegramUser telegramUser2;

    @BeforeEach
    void setUp() {
        telegramUserRepo.deleteAllInBatch();

        telegramUser1 = buildUser(1111111111L, "Ivan", "Ivanov", "Ivano", "ru");
        telegramUser2 = buildUser(2222222222L, "Jon", "Black", "Smith", "ru");
        telegramUserRepo.saveAll(List.of(telegramUser1, telegramUser2));
    }

    @Test
    @DisplayName("Сохранение и поиск по id")
    void testSaveAndFindById() {
        Optional<TelegramUser> foundTelegramUser = telegramUserRepo.findById(telegramUser1.getId());

        assertTrue(foundTelegramUser.isPresent());
        assertEquals(telegramUser1.getId(), foundTelegramUser.get().getId());
        assertEquals(telegramUser1.getUserName(), foundTelegramUser.get().getUserName());
    }

    @Test
    @DisplayName("Сохранение и поиск всех пользователей")
    void testSaveAndFindAll() {
        List<TelegramUser> telegramUserList = telegramUserRepo.findAll();

        assertEquals(2, telegramUserList.size());
        assertTrue(telegramUserList.stream().anyMatch(m -> m.getId() == 1111111111L));
        assertTrue(telegramUserList.stream().anyMatch(m -> m.getId() == 2222222222L));
    }

    @Test
    @DisplayName("Удаление по id")
    void testDeleteById() {
        telegramUserRepo.deleteById(telegramUser1.getId());

        Optional<TelegramUser> foundTelegramUser = telegramUserRepo.findById(telegramUser1.getId());

        assertFalse(foundTelegramUser.isPresent());
    }

    @Test
    @DisplayName("Удаление всех пользователей")
    void testDeleteAll() {
        telegramUserRepo.deleteAll();

        List<TelegramUser> allTelegramUsers = telegramUserRepo.findAll();

        assertEquals(0, allTelegramUsers.size());
    }

    @Test
    @DisplayName("Проверка количества пользователей")
    void testCount() {
        long count = telegramUserRepo.count();

        assertEquals(2, count);
    }

    @Test
    @DisplayName("Проверка существования по id")
    void testExistsById() {
        boolean exists = telegramUserRepo.existsById(telegramUser1.getId());

        assertTrue(exists);
    }

    @Test
    @DisplayName("Проверка несуществующего id")
    void testExistsById_NotExists() {
        boolean exists = telegramUserRepo.existsById(999999L);

        assertFalse(exists);
    }

    @Test
    @DisplayName("Массовое сохранение пользователей")
    void testSaveAll() {
        TelegramUser user3 = buildUser(3333333333L, "Alice", "Smith", "alice", "en");
        TelegramUser user4 = buildUser(4444444444L, "Bob", "Brown", "bob", "en");

        List<TelegramUser> users = List.of(user3, user4);
        List<TelegramUser> savedUsers = telegramUserRepo.saveAll(users);

        assertEquals(2, savedUsers.size());
        assertEquals(4, telegramUserRepo.count());
    }

    @Test
    @DisplayName("Поиск по списку id")
    void testFindAllById() {
        List<Long> ids = List.of(telegramUser1.getId(), telegramUser2.getId());
        List<TelegramUser> telegramUserList = telegramUserRepo.findAllById(ids);

        assertEquals(2, telegramUserList.size());
        assertTrue(telegramUserList.stream().anyMatch(m -> m.getId() == 1111111111L));
        assertTrue(telegramUserList.stream().anyMatch(m -> m.getId() == 2222222222L));
    }

    @ParameterizedTest(name = "Сохранение пользователя: {0}")
    @MethodSource("edgeCaseUsersProvider")
    @DisplayName("Параметризованный тест сохранения пользователей")
    void testSaveEdgeCaseUsers(TelegramUser user) {
        TelegramUser saved = telegramUserRepo.save(user);

        Optional<TelegramUser> found = telegramUserRepo.findById(saved.getId());

        assertTrue(found.isPresent());
        assertEquals(user.getUserName(), found.get().getUserName());
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

    static Stream<TelegramUser> edgeCaseUsersProvider() {
        return Stream.of(
                buildUser(0L, "", "", "", ""),
                buildUser(-1L, "Negative", "User", "neg", "xx"),
                buildUser(5555555555L, "Тест", "Русский", "russian", "ru"),
                buildUser(6666666666L, "Special", "Chars", "user!@#", "en")
        );
    }
}